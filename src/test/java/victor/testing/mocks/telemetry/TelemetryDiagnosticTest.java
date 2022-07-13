package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//  @Test
//   public void configuresClient() throws Exception {
//      when(client.getOnlineStatus()).thenReturn(true);
//      LocalDateTime testTime = now();
//
//      try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
//         mockedStatic.when(LocalDateTime::now).thenReturn(testTime);// only do this if
//         // the tested code does time-based calculations. eg now(). minus 1 day.
//
//         target.checkTransmission(true);
//      }
//
//      ArgumentCaptor<ClientConfiguration> captor = forClass(ClientConfiguration.class);
//      verify(client).configure(captor.capture());
//      ClientConfiguration config = captor.getValue();
//      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
//      // how to asset time
//
//      assertThat(config.getSessionStart()).isNotNull();// engineer way
//
//      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS)); // scientific way
//
//      assertThat(config.getSessionStart()).isEqualTo(testTime); //
//      // GEEK WAY:
//      // ideas to exactly control the time in prod code
//      // 1) @Spy a local method - NOPE
//      // 2) inject a Clock
//      // 3) pass LocalDateTime as arg to method
//      // 4) stub the static method LocalDateTime.now() previously with PowerMockito, but recently with mockito-inline
//   }

//@MockitoSettings(strictness = Strictness.LENIENT) // NO: because opens the door for a @Before orf 10 when..then
@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
   @Mock
   private TelemetryClient client /*= new TelemetryClient() {
      @Override
      public boolean getOnlineStatus() {
         return true;
      }
   } */
   /*= mock(TelemetryClient.class)*/; // a mock is created and injected here
   @InjectMocks
   @Spy
   private TelemetryDiagnostic target; // the mock is injected in the dependecy of target

   @BeforeEach
   final void before() {
      lenient().when(client.getOnlineStatus()).thenReturn(true);
   }

   @Test
   public void disconnects() {
      // what the heck happens here in fact (the line above)


      doReturn(new ClientConfiguration()).when(target).configureClient(any());

      target.checkTransmission(true);

      verify(client).disconnect(true); // mocking = verification that a method was invoked
   }

   @Test
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      Assertions.assertThrows(IllegalStateException.class, () ->
          target.checkTransmission(true));
   }

   @Test
   public void sendsDiagnosticInfo() {
      target.checkTransmission(true);
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }

   @Test
   public void receivesDiagnosticInfo() {
      // TODO inspect
      when(client.receive()).thenReturn("granpa"); // stubbing

      // act
      target.checkTransmission(true);

      assertThat(target.getDiagnosticInfo()).isEqualTo("granpa");
      verify(client).receive(); // should be needed,
      // Why would you ever want to verify() a method that you also when.,.then (stubbed)
      // to make sure the function that brought data to tested code is called only ONCE
      // WHY?
      // - expensive $ or time
      // - not referential transparent (diffrerent results 2nd time: relies on time, random, reads over NETWORK)
      // BUT if the method is NOT doing network => having  to verify() it means it's not CQS
   }

   @Test
   public void configuresClient() throws Exception {
      ClientConfiguration config = target.configureClient("ver");

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS)); // scientific way
      assertThat(config.getSessionId())
              .startsWith("VER-")
              .hasSizeGreaterThan(10);
   }
}

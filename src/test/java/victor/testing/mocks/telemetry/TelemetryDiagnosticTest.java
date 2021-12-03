package victor.testing.mocks.telemetry;

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
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // @RunWith(MockitoJUnitRunner)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TelemetryDiagnosticTest {
//   private TelemetryClient clientMock;
   @Mock
   private TelemetryClient clientMock;// = Mockito.mock(TelemetryClient.class);

   @InjectMocks
   @Spy
   private TelemetryDiagnostic target;

   @BeforeEach
   final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true); // useful for the majority of @Test below
//      when(clientMock.getVersion()).thenReturn("wtf do i need this?!@&$&&*&*@");
   }
   
   @Test
   void disconnects() {
//      Mockito.doReturn("aaa").when(clientMock.getOnlineStatus());  // NEVER use this
      // doReturn is only used when using @Spy (partial mocks) which are ANTI-PATTERNS
      doReturn(new ClientConfiguration()).when(target).createConfig();

      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
   }

   @Test
   void throwsWhenNotOnline() {
      // given
      doReturn(new ClientConfiguration()).when(target).createConfig();

      when(clientMock.getOnlineStatus()).thenReturn(false); // overrides the stubbed behavior from beforeEach

      //when
      assertThatThrownBy(() -> target.checkTransmission(true));
   }

   @Test
   void send() {
      doReturn(new ClientConfiguration()).when(target).createConfig();

      target.checkTransmission(true);

      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // most readable < pick this usually
      verify(clientMock).send("AT#UD"); // prefer this when the data goes to an external system (is part of a protocl)
      verify(clientMock).send(anyString()); // weakest
   }
   
   @Test
   void receives() {
      // given
      when(clientMock.receive()).thenReturn("::receivedData::");
      doReturn(new ClientConfiguration()).when(target).createConfig();

      // when
      target.checkTransmission(true);

      //then
      verify(clientMock).receive();  //became usless when I checked what happened with the returned value  (next line)
      // verify of a method that you did when.thenReturn is acceptable only if
      // 1) it has significant Side effects (eg INSERT) - means the method is violating the CQS principle
      // 2) performance: if it's a HTTP/SOAP network call, you want to make sure it happens just once

      assertThat(target.getDiagnosticInfo()).isEqualTo("::receivedData::");
   }
//      verify(clientMock, never()).paidCall(); // $
//      verify(clientMock, never()).reportBadLoanPayer(customerId); // critical damaging side effect

   @Captor
   ArgumentCaptor<ClientConfiguration> captor;

// alternative to captors:   verify(clientMock).configure(argThat(config -> config.getAckMode().equals(AckMode.NORMAL)));

      // in case the class you are testing is part of a public API of a library,
      // used by unknown devs
      //      TelemetryDiagnostic.class.getDeclaredMethod("createConfig");
   @Test
   void createConfigOk() {
      //given
      when(clientMock.getVersion()).thenReturn("ver");

      //when - prod call
      ClientConfiguration config = target.createConfig();

      // then
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
//      assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now()); // don't
      assertThat(config.getSessionStart()).isNotNull();
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS)); // 5% best developers
      assertThat(config.getSessionId())
          .startsWith("VER-")
          .hasSizeGreaterThan(10);
   }


   // CR-1424 : the client version in session id should be only in uppercase
}

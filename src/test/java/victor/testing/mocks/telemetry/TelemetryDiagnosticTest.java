package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
   @Mock
   private TelemetryClient clientMock; /*=  new TelemetryClient() {
      @Override
      public boolean getOnlineStatus() {
         return true;
      }
   };*/
   @InjectMocks
   private TelemetryDiagnostic target;

   @Test
   void ok() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      // dynamic params
      when(clientMock.receive()).thenReturn("::message::");

      target.checkTransmission(true);

//      try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//         // alternative for Junit4:  @Rule  public final VerificationCollector collector = MockitoJUnit.collector();
//         // https://stackoverflow.com/questions/53694359/how-to-use-soft-assertions-in-mockito
//         softly.assertThat(target.getDiagnosticInfo()).isEqualTo("::message::");
//         softly.assertThatThrownBy(() -> verify(clientMock).disconnect(true)).doesNotThrowAnyException();
//         softly.assertThatThrownBy(() -> verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE)).doesNotThrowAnyException();
//         softly.assertThatThrownBy(() -> verify(clientMock).receive()).doesNotThrowAnyException(); // do not verify stubbing  -redundant
//      }

      assertThat(target.getDiagnosticInfo()).isEqualTo("::message::");
      verify(clientMock).disconnect(true);
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      verify(clientMock).receive(); // do not verify stubbing  -redundant

      // CQS prinicple

       // TODO ...
   }

   @Captor
   ArgumentCaptor<ClientConfiguration> captor;

   @Test
   void captors() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.getVersion()).thenReturn("ver");

      target.checkTransmission(true);

      verify(clientMock).configure(captor.capture());
      ClientConfiguration config = captor.getValue();

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
//      assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now()); //flaky
      assertThat(config.getSessionStart()).isNotNull(); // 95% asserting time
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, MINUTES)); // scinece guy
      assertThat(config.getSessionId())
          .startsWith("ver-")
          .hasSizeGreaterThan(10);
   }
   @Test
   void testingPureFunctionsIsEasier() {
     ClientConfiguration config = target.createConfig("ver");

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, MINUTES)); // scinece guy
      assertThat(config.getSessionId())
          .startsWith("ver-")
          .hasSizeGreaterThan(10);

   }
}

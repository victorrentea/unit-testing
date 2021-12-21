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
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // avoid at class level
// consider Mockito.lenient().when....
public class TelemetryDiagnosticCheckTransmissionTest {
   @Mock
   private TelemetryClient clientMock; /*=  new TelemetryClient() {
      @Override
      public boolean getOnlineStatus() {
         return true;
      }
   };*/
   @InjectMocks
   private TelemetryDiagnostic target;

   @BeforeEach
   final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.getVersion()).thenReturn("why the hack do i need this heer ?");

   }

   @Test
   void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false); // eception to the rule: re-programming the mock

      assertThatThrownBy(() -> target.checkTransmission(true))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("connect");
   }

   @Test
   void ok() {
      // dynamic params
      when(clientMock.receive()).thenReturn("::message::");

      target.checkTransmission(true);

      assertThat(target.getDiagnosticInfo()).isEqualTo("::message::");
      verify(clientMock).disconnect(true);
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);

      verify(clientMock).receive(); // ONLY DO THIS IF THE FUNCTION:  you are violating CQS principle
      // 1 *** SIDE EFFECT = POST > CREATE
      // 2 EVERY CALL CO$TS MONEY
      // 3 TAKES TIME
   }

   @Captor
   ArgumentCaptor<ClientConfiguration> captor;

   @Test
   void captors() {
      when(clientMock.getVersion()).thenReturn("ver");

      target.checkTransmission(true);

      verify(clientMock).configure(captor.capture());
      ClientConfiguration config = captor.getValue();

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
//      assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now()); //flaky
      assertThat(config.getSessionStart()).isNotNull(); // 95% asserting time
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, MINUTES)); // scinece guy
      assertThat(config.getSessionId())
          .startsWith("VER-")
          .hasSizeGreaterThan(10);
   }
   @Test
   void testingPureFunctionsIsEasier() { // TODO +7 more test
      ClientConfiguration config = target.createConfig("ver");

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, MINUTES)); // scinece guy
      assertThat(config.getSessionId())
          .startsWith("VER-")
          .hasSizeGreaterThan(10);
   }
}

class TelemetryDiagnosticCreateConfigTest {

   TelemetryDiagnostic target = new TelemetryDiagnostic(null);


}
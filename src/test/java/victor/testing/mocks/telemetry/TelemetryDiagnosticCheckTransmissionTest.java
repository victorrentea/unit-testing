package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT) // avoid at class level
// consider Mockito.lenient().when....
public class TelemetryDiagnosticCheckTransmissionTest {
   @Mock
   private TelemetryClient clientMock; /*=  new TelemetryClient() {
      @Override
      public boolean getOnlineStatus() {
         return true;
      }
   };*/
   @Mock
   ClientConfigurationFactory factoryMock;//  = new ClientConfigurationFactory();

   @InjectMocks
   private TelemetryDiagnostic target;

   @BeforeEach
   final void before() {


      when(clientMock.getOnlineStatus()).thenReturn(true);
//      when(clientMock.getVersion()).thenReturn("why the hack do i need this heer ?");
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
//      doReturn(new ClientConfiguration()).when(target).createConfig(any());

      target.checkTransmission(true);

      assertThat(target.getDiagnosticInfo()).isEqualTo("::message::");
      verify(clientMock).disconnect(true);
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);

      verify(clientMock).receive(); // ONLY DO THIS IF THE FUNCTION:  you are violating CQS principle
      // 1 *** SIDE EFFECT = POST > CREATE
      // 2 EVERY CALL CO$TS MONEY
      // 3 TAKES TIME
   }

}

class TelemetryDiagnosticCreateConfigTest {

   ClientConfigurationFactory target = new ClientConfigurationFactory();

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
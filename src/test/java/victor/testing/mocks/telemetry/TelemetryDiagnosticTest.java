package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // in practica nu o face
public class TelemetryDiagnosticTest {
   @Mock
   TelemetryClient clientMock;
   @InjectMocks
   TelemetryDiagnostic target;

   @BeforeEach
   final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
   }


   @Test
   public void multiAssertPerTest() {
      when(clientMock.receive()).thenReturn("::tataie::"); // stubbing pe receive

      target.checkTransmission(true);

      // 2-4 assertii per test.   "Single Assert Rule"
      verify(clientMock).disconnect(true);
      verify(clientMock).send(eq(TelemetryClient.DIAGNOSTIC_MESSAGE), any());
      AssertionsForClassTypes.assertThat(target.getDiagnosticInfo()).isEqualTo("::tataie::");
   }

   @Test
   public void disconnects() {
      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
   }
   @Test
   public void sendsDiagnosticMessage() {
      target.checkTransmission(true);

      verify(clientMock).send(
          eq(TelemetryClient.DIAGNOSTIC_MESSAGE),
          any());
   }

   @Test
   public void receivesDiagnosticInfo() {
      when(clientMock.receive()).thenReturn("::tataie::"); // stubbing pe receive

      target.checkTransmission(true);

      AssertionsForClassTypes.assertThat(target.getDiagnosticInfo()).isEqualTo("::tataie::");
//      verify(clientMock).receive(); // 99% nu e nevoie sa-i faci si verify() daca i-ai facut when...then
      // care-i aia <1%? pe api externe
   }

   @Test
   public void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false); // reprogramam mockul

      assertThrows(IllegalStateException.class,
          () -> target.checkTransmission(true));
   }

   @Test
   void configuresClient() {
      when(clientMock.getVersion()).thenReturn("ver");

      target.checkTransmission(true);

      ArgumentCaptor<ClientConfiguration> configCaptor = forClass(ClientConfiguration.class);
      verify(clientMock).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      Assertions.assertThat(config.getSessionStart()).isNotNull();
      Assertions.assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(5, SECONDS));
      assertThat(config.getSessionId())
          .startsWith("ver-")
          .hasSizeGreaterThan(10);
   }


   @Test
   void configuresClientDirectCall() {
      ClientConfiguration config = target.createConfig("ver");

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      Assertions.assertThat(config.getSessionStart()).isNotNull();
      Assertions.assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(5, SECONDS));
      assertThat(config.getSessionId())
          .startsWith("ver-")
          .hasSizeGreaterThan(10);

   }
}
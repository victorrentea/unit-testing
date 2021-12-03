package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TelemetryDiagnosticHolisticTest {
   TelemetryClient clientMock = mock(TelemetryClient.class);
   ClientConfigurationFactory configurationFactoryReal = new ClientConfigurationFactory();
   TelemetryDiagnostic target = new TelemetryDiagnostic(clientMock, configurationFactoryReal);

   @Captor
   ArgumentCaptor<ClientConfiguration> configCaptor;

   @BeforeEach
   final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.getVersion()).thenReturn("ver");
   }

   @Test
   void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false);

      assertThatThrownBy(() -> target.checkTransmission(true));
   }

   @Test
   void happy() {
      when(clientMock.receive()).thenReturn("::receivedData::");

      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      assertThat(target.getDiagnosticInfo()).isEqualTo("::receivedData::");
   }

   @Test
   void configuresOk() {
      target.checkTransmission(true);

      verify(clientMock).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
      assertThat(config.getSessionId())
          .startsWith("VER-")
          .hasSizeGreaterThan(10);
   }
}
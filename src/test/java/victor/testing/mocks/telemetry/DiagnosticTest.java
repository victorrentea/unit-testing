package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DiagnosticTest {
   @Mock
   private Client client;
   @InjectMocks
   private Diagnostic target;

   @BeforeEach
   final void before() {
      when(client.getOnlineStatus()).thenReturn(true);
   }

  @Test
  void disconnects() {
      target.checkTransmission(true);
      verify(client).disconnect(true);
   }

  @Test
  void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      assertThatThrownBy(() -> target.checkTransmission(true))
              .isInstanceOf(IllegalStateException.class);
   }

  @Test
  void sendsDiagnosticInfo() {
      target.checkTransmission(true);
      verify(client).send(Client.DIAGNOSTIC_MESSAGE);
   }

  @Test
  void receivesDiagnosticInfo() {
      final String diagnosticMessage = "DIAG";
      when(client.receive()).thenReturn(diagnosticMessage);
      target.checkTransmission(true);
      verify(client).receive();
      assertThat(target.getDiagnosticInfo()).isEqualTo(diagnosticMessage);
   }

   @Captor
   ArgumentCaptor<ClientConfiguration> configCaptor;

  @Test
  void configuresClient() throws Exception {
      when(client.getVersion()).thenReturn("ver");

      target.checkTransmission(true);

      verify(client).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
      assertThat(config.getSessionId()).startsWith("ver-").hasSize("ver-".length() + 36);
   }
}

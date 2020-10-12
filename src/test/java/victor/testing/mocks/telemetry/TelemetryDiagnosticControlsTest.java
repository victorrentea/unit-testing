package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticControlsTest {
   @Mock
   private TelemetryClient client;
   @InjectMocks
   private TelemetryDiagnosticControls controls;

   @BeforeEach
   public void initialize() {
      when(client.getVersion()).thenReturn("ver");
   }
   @Test
   public void disconnects() {
      when(client.getOnlineStatus()).thenReturn(true);
      controls.checkTransmission();
      verify(client).disconnect();
   }

   @Test
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      Assertions.assertThrows(IllegalArgumentException.class,
          () -> controls.checkTransmission());
   }

   @Test
   public void sendsDiagnosticInfo() {
      when(client.getOnlineStatus()).thenReturn(true);
      controls.checkTransmission();
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }

   // TODO TEMA: ce linie din urm metoda poate fi stearsa
   @Test
   public void receivesDiagnosticInfo() {
      // TODO inspect
      when(client.getOnlineStatus()).thenReturn(true);
      when(client.receive()).thenReturn("tataie");
      controls.checkTransmission();
      assertThat(controls.getDiagnosticInfo()).isEqualTo("tataie");
   }

   @Captor
   private ArgumentCaptor<ClientConfiguration> configCaptor;
   @Test
   public void configuresClient() throws Exception {
      when(client.getOnlineStatus()).thenReturn(true);
      controls.checkTransmission();


      verify(client).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
   }

   @Test
   public void createsCorrectConfig() {
      ClientConfiguration config = controls.createConfig("ver");
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);

   }
}

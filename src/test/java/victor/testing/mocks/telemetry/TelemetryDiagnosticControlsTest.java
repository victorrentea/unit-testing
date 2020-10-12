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
public class TelemetryDiagnosticControlsTest extends TestBase{
   @Mock
   private TelemetryClient client;
   @Mock
   private ConfigurationFactory configurationFactory;
   @InjectMocks
   private TelemetryDiagnosticControls controls;

   @BeforeEach
   public void initialize2() {
      System.out.println("initul meu");
      when(client.getOnlineStatus()).thenReturn(true);
   }
   @Test
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      Assertions.assertThrows(IllegalStateException.class,
          () -> controls.checkTransmission());
   }

   @Test
   public void sendsDiagnosticInfo() {
      when(client.receive()).thenReturn("tataie");
      controls.checkTransmission();
      verify(client).disconnect();
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      assertThat(controls.getDiagnosticInfo()).isEqualTo("tataie");
   }

   @Test
   public void configuresClient() {
      when(client.getVersion()).thenReturn("ver");
      controls.checkTransmission();
      verify(configurationFactory).createConfig("ver");
   }

}


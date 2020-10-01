package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public class TelemetryDiagnosticControlsTest {

   @Test(expected = IllegalStateException.class)
   public void test() {
      TelemetryClient client = Mockito.mock(TelemetryClient.class);

      Mockito.when(client.getOnlineStatus()).thenReturn(false);

      TelemetryDiagnosticControls controls =
          new TelemetryDiagnosticControls(client);
      controls.checkTransmission();
   }

   @Test
   public void disconnects() {
      TelemetryClient client = Mockito.mock(TelemetryClient.class);

      Mockito.when(client.getOnlineStatus()).thenReturn(true);

      TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls(client);

      controls.checkTransmission();

      Mockito.verify(client).disconnect();
   }
}

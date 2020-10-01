package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public class TelemetryDiagnosticControlsTest {

   @Test(expected = IllegalStateException.class)
   public void test() {
//      TelemetryClient client = new TelemetryClient() {
//         @Override
//         public void disconnect() {
//            //nada
//         }
//
//         @Override
//         public boolean getOnlineStatus() {
//            return false;
//         }
//      };
      TelemetryClient client = Mockito.mock(TelemetryClient.class);

      Mockito.when(client.getOnlineStatus()).thenReturn(false);

      TelemetryDiagnosticControls controls =
          new TelemetryDiagnosticControls(client);
      controls.checkTransmission();
   }
}

package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public class TelemetryDiagnosticControlsTest {

   @Test
   public void test() {
      TelemetryDiagnosticControls controls =
          new TelemetryDiagnosticControls(new TelemetryClient(){
             @Override
             public void disconnect() {
                //nada
             }

             @Override
             public boolean getOnlineStatus() {
                return false;
             }
          });
      controls.checkTransmission();
   }
}

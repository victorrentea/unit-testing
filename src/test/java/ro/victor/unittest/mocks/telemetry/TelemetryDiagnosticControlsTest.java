package ro.victor.unittest.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
   @Mock
   private TelemetryClient client;
   @InjectMocks
   private TelemetryDiagnosticControls controls;

   @Before
   public void initialize() {
      when(client.getOnlineStatus()).thenReturn(true);
   }

   @Test(expected = IllegalStateException.class)
   public void test() {
      when(client.getOnlineStatus()).thenReturn(false);
      controls.checkTransmission();
   }

   @Test
   public void disconnects() {
      controls.checkTransmission();
      verify(client).disconnect();
   }
   @Test
   public void sends() {
      controls.checkTransmission();
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }
}

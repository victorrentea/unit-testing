package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
   @Mock
   private TelemetryClient client;
   @InjectMocks
   private TelemetryDiagnosticControls controls;

   @Test
   public void disconnects() {
      when(client.getOnlineStatus()).thenReturn(true);
      controls.checkTransmission();
      verify(client).disconnect();
   }

   @Test(expected = IllegalStateException.class)
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      controls.checkTransmission();
   }

   @Test
   public void sendsDiagnosticInfo() {
      when(client.getOnlineStatus()).thenReturn(true);
      controls.checkTransmission();
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }

   @Test
   public void receivesDiagnosticInfo() {
      // TODO inspect
      when(client.getOnlineStatus()).thenReturn(true);
      when(client.receive()).thenReturn("tataie");
      controls.checkTransmission();
      verify(client).receive();
      assertThat(controls.getDiagnosticInfo()).isEqualTo("tataie");
   }

   @Test
   public void configuresClient() throws Exception {
      when(client.getOnlineStatus()).thenReturn(true);
      controls.checkTransmission();
      verify(client).configure(any());
      // TODO check config.getAckMode is NORMAL
   }
}

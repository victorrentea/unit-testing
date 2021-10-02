package victor.testing.mocks.telemetry;

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
public class TelemetryControllerTest {
   @Mock
   private TelemetryClient client;
   @InjectMocks
   private TelemetryController target;

   @Test
   public void disconnects() {
      when(client.getOnlineStatus()).thenReturn(true);
      target.checkTransmission(true);
      verify(client).disconnect(true);
   }

   @Test(expected = IllegalStateException.class)
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      target.checkTransmission(true);
   }

   @Test
   public void sendsDiagnosticInfo() {
      when(client.getOnlineStatus()).thenReturn(true);
      target.checkTransmission(true);
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }

   @Test
   public void receivesDiagnosticInfo() {
      // TODO inspect
      when(client.getOnlineStatus()).thenReturn(true);
      when(client.receive()).thenReturn("tataie");
      target.checkTransmission(true);
      verify(client).receive();
      assertThat(target.getDiagnosticInfo()).isEqualTo("tataie");
   }

   @Test
   public void configuresClient() throws Exception {
      when(client.getOnlineStatus()).thenReturn(true);
      target.checkTransmission(true);
      verify(client).configure(any());
      // TODO check config.getAckMode is NORMAL
   }
}

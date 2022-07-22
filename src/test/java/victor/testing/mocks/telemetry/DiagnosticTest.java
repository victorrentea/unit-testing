package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DiagnosticTest {
   @Mock
   private Client client;
   @InjectMocks
   private Diagnostic target;

   @Test
   public void disconnects() {
      when(client.getOnlineStatus()).thenReturn(true);
      target.checkTransmission(true);
      verify(client).disconnect(true);
   }

   @Test
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      assertThatThrownBy(() ->
              target.checkTransmission(true)).isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void sendsDiagnosticInfo() {
      when(client.getOnlineStatus()).thenReturn(true);
      target.checkTransmission(true);
      verify(client).send(Client.DIAGNOSTIC_MESSAGE);
   }

   @Test
   public void receivesDiagnosticInfo() {
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

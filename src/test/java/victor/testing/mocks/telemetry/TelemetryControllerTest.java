package victor.testing.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryControllerTest {
   @Mock
   private TelemetryClient client;
   @InjectMocks
   private TelemetryController target;

   @Before
   public final void before() {
      when(client.getVersion()).thenReturn("for nothing");
   }

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
      when(client.receive()).thenReturn("tataieAD");
      target.checkTransmission(true);
      verify(client).receive(); // the next line renders this one useless - DELETE IT
      // verify on a when.then ONLY MAKES sense if that method also has some side effects
      // >> CQS principle
      assertThat(target.getDiagnosticInfo()).isEqualTo("tataieAD");
   }

   @Test
   public void configuresClient() throws Exception {
      when(client.getOnlineStatus()).thenReturn(true);

      target.checkTransmission(true);

      verify(client).configure(notNull());
   }

   @Test
   public void configurationCreatedOk() { // x20 tests
      ClientConfiguration config = target.createConfig("ver");
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionId()).startsWith("VER-");
   }

   @Captor
   ArgumentCaptor<ClientConfiguration> configCaptor;
}

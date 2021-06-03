package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Configurable;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;
import victor.testing.spring.tools.WireMockExtension;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TelemetryDiagnosticControlsTest {
   @Mock
   private TelemetryClient client;
   private ClientConfigurationFactory configurationFactory = new ClientConfigurationFactory();
   private TelemetryDiagnosticControls controls;
   @RegisterExtension // ~ @Rule in Junit4
   public WireMockExtension wireMock = new WireMockExtension(9999);

   @BeforeEach
   public final void before() {
      when(client.getOnlineStatus()).thenReturn(true);
//      when(client.getVersion()).thenReturn("cevaNatiaCaEVineriSeara");
      controls = new TelemetryDiagnosticControls(client, configurationFactory);
   }

   @Test
   public void disconnects() {
//      when(client.method()).thenReturn(Collections.emptyList());

      controls.checkTransmission(true);

      verify(client).disconnect(true);
   }

   @Test
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      assertThatThrownBy(() -> controls.checkTransmission(true))
          .isInstanceOf(IllegalStateException.class)
//          .matches(e -> e.getCause == CANNOT_CONNECT)
          .hasMessageContaining("connect");
   }

   @Test
   public void receivesDiagnosticInfo() {
      // TODO inspect
      when(client.receive()).thenReturn("tataie");

      controls.checkTransmission(true);

//      verify(client).receive();
      assertThat(controls.getDiagnosticInfo()).isEqualTo("tataie");
   }

   @Test
   public void sendsDiagnosticInfo() {
      controls.checkTransmission(true);
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
//      verify(client, never()).raporteazaFrauda();
   }

   @Captor
   private ArgumentCaptor<ClientConfiguration> configCaptor;

   @Test
   public void configuresClient() throws Exception {
      controls.checkTransmission(true);

      verify(client).configure(any());
   }
}
class ClientConfigurationFactoryTest {
   @Test
   public void createConfig() throws Exception {
      ClientConfiguration config = new ClientConfigurationFactory().createConfig("ver");

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart()).isNotNull();
//      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));

      assertThat(config.getSessionId()).startsWith("VER-");
   }

}

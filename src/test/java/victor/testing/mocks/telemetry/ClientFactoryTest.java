package victor.testing.mocks.telemetry;

import org.junit.Test;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientFactoryTest {
   @Test
   public void configurationCreatedOk() { // x20 tests
      ClientConfiguration config = new ConfigFactory().createConfig("ver");
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionId()).startsWith("VER-");
   }

}

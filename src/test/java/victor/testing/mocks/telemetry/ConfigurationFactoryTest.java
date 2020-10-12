package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationFactoryTest {

   private ConfigurationFactory factory;

   // nici un mock :)
   @Test
   public void createsCorrectConfig() {
      factory = new ConfigurationFactory();
      ClientConfiguration config = factory.createConfig("ver");
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);

   }
}

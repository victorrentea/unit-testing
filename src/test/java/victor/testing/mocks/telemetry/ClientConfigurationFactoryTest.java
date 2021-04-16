package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientConfigurationFactoryTest {

   private ClientConfigurationFactory factory = new ClientConfigurationFactory();

   @Test
   public void configurationIsCorrect() {
      ClientConfiguration config = factory.createConfig("vers");
      assertEquals(AckMode.NORMAL, config.getAckMode());
      assertThat(config.getSessionId())
          .startsWith("VERS-")
          .hasSizeGreaterThan(7);
//      assertTrue(config.getSessionId().startsWith("vers-"));
      assertThat(config.getSessionStart()).isNotNull();
//      Assertions.assertThat(config.getSessionStart()).isCloseTo(LocalDateTime.now(), new TUO);
      assertThat(config.getSessionStart()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));

   }

}

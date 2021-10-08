package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode.NORMAL;

class ConfigFactoryTest {

   @Test
   public void configuresClientCorrectly() {
      ClientConfiguration config = new ConfigFactory().createConfig("ver");

      assertThat(config.getAckMode()).isEqualTo(NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(now(), within(1, MINUTES));
      assertThat(config.getSessionId()).startsWith("VER-");
   }
}
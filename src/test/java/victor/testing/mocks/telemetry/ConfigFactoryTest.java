package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ConfigFactoryTest {

   @Test
   public void configuresClientCorrectly() {
      ClientConfiguration config = new ConfigFactory().createConfig("ver");

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
      assertThat(config.getSessionId()).startsWith("VER-");
   }
}
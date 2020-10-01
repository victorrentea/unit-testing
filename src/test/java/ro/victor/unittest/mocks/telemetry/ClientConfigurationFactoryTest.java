package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class ClientConfigurationFactoryTest {

   @Test
   public void createConfig() {
      ClientConfiguration config = new ClientConfigurationFactory().createConfig("xyz");
      assertEquals(AckMode.NORMAL, config.getAckMode());
      assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS),
          config.getSessionStart().truncatedTo(ChronoUnit.DAYS));
      assertThat(config.getSessionId()).startsWith("XYZ-");
   }
}

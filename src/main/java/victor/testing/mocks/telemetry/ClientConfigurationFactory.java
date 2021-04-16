package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClientConfigurationFactory {

   public ClientConfiguration createConfig(String version) {
      ClientConfiguration config = new ClientConfiguration(); // Test-induced design damage
      config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
      config.setSessionStart(LocalDateTime.now());
      // IMAGINE there's NO  complexity
      config.setAckMode(AckMode.NORMAL);
      return config;
   }

}

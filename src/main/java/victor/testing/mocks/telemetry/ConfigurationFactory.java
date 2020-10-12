package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConfigurationFactory {

   // pure function: aceleasi inputuri aceleasi output, no side effects
   public ClientConfiguration createConfig(String version) {
      ClientConfiguration config = new ClientConfiguration();
      // Imaginati 20 de linii de logica grea
      config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
      config.setSessionStart(LocalDateTime.now());
      config.setAckMode(AckMode.NORMAL);
      return config;
   }
}

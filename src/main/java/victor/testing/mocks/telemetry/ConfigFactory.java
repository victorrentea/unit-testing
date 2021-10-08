package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConfigFactory {
   public ClientConfiguration createConfig(String version) {
      ClientConfiguration config = new ClientConfiguration();
      // multa logica
      // multa logica
      // multa logica
      // multa logica
      // multa logica
      config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID());
      config.setSessionStart(LocalDateTime.now());
      config.setAckMode(AckMode.NORMAL); // ASTA
      return config;
   }
}

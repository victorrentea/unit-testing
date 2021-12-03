package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryDiagnostic {
   public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

   private final TelemetryClient client;
   private final ClientConfigurationFactory configurationFactory;
   private String diagnosticInfo = "";
//	private ClientConfiguration config; // BAD = design damage: add mutable state NEVER!!🩸 + break encapsulation just for tests
//	ClientConfiguration getConfig() {
//		return config;
//	}

   public TelemetryDiagnostic(TelemetryClient telemetryClient, ClientConfigurationFactory configurationFactory) {
      this.client = telemetryClient;
      this.configurationFactory = configurationFactory;
   }


   public String getDiagnosticInfo() {
      return diagnosticInfo;
   }

   public void setDiagnosticInfo(String diagnosticInfo) {
      this.diagnosticInfo = diagnosticInfo;
   }

   public void checkTransmission(boolean force) {
      client.disconnect(force);

      int currentRetry = 1;
      while (!client.getOnlineStatus() && currentRetry <= 3) {
         client.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
         currentRetry++;
      }

      if (!client.getOnlineStatus()) {
         throw new IllegalStateException("Unable to connect.");
      }

      ClientConfiguration config = configurationFactory.createConfig(client.getVersion());
      client.configure(config);

      client.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      diagnosticInfo = client.receive();
   }

}

class ClientConfigurationFactory {

   public ClientConfiguration createConfig(String version) {
      ClientConfiguration config = new ClientConfiguration();
      config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID());
      config.setSessionStart(LocalDateTime.now());
      // HARD-CORE 20 lines of logic, we need 8 tests to pass through this logic
      config.setAckMode(AckMode.NORMAL);
      return config;
   }
}

package victor.testing.mocks.telemetry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

@Service
@RequiredArgsConstructor
public class TelemetryDiagnostic {
   private final TelemetryClient telemetryClient;
   private final ConfigFactory configFactory;

   public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

   private String diagnosticInfo = "";

   public String getDiagnosticInfo() {
      return diagnosticInfo;
   }

   public void setDiagnosticInfo(String diagnosticInfo) {
      this.diagnosticInfo = diagnosticInfo;
   }

   public void checkTransmission(boolean force) {
      telemetryClient.disconnect(force);

      int currentRetry = 1;
      while (!telemetryClient.getOnlineStatus() && currentRetry <= 3) {
         telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
         currentRetry++;
      }

      if (!telemetryClient.getOnlineStatus()) {
         throw new IllegalStateException("Unable to connect.");
      }

      ClientConfiguration config = configFactory.createConfig(telemetryClient.getVersion());
      telemetryClient.configure(config);

      telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      diagnosticInfo = telemetryClient.receive();
   }


}



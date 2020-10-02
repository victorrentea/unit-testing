package ro.victor.unittest.mocks.telemetry;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryDiagnosticControls {
   public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

   private final TelemetryClient telemetryClient;
   private final ClientConfigurationFactory configurationFactory;
   private String diagnosticInfo = "";

   public TelemetryDiagnosticControls(TelemetryClient telemetryClient, ClientConfigurationFactory configurationFactory) {
      this.telemetryClient = telemetryClient;
      this.configurationFactory = configurationFactory;
   }

   public String getDiagnosticInfo() {
      return diagnosticInfo;
   }

   public void setDiagnosticInfo(String diagnosticInfo) {
      this.diagnosticInfo = diagnosticInfo;
   }

   public void checkTransmission() {// 8 teste intra pe aici
      // TODO extract reconnect()

      telemetryClient.disconnect();

      int currentRetry = 1;
      while (!telemetryClient.getOnlineStatus() && currentRetry <= 3) {
         telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
         currentRetry++;
      }

      if (!telemetryClient.getOnlineStatus()) {
         throw new IllegalStateException("Unable to connect.");
      }

      ClientConfiguration config = configurationFactory.createConfig(telemetryClient.getVersion());
      telemetryClient.configure(config);


      telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      diagnosticInfo = telemetryClient.receive();
   }

}




class ClientConfigurationFactory {
   public ClientConfiguration createConfig(String version) { // 10 teste intra pe aici
      ClientConfiguration config = new ClientConfiguration();
      config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
      config.setSessionStart(LocalDateTime.now());
      //20 de lini de logica grea
      config.setAckMode(AckMode.NORMAL);
//      ejb.callStuff(xxxx);
      return config;
   }
//   @EJB
//   MyEJB ejb;

}

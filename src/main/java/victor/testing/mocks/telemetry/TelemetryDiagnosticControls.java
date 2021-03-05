package victor.testing.mocks.telemetry;

import org.springframework.web.client.RestTemplate;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryDiagnosticControls {
   public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

   private final TelemetryClient telemetryClient;
   private String diagnosticInfo = "";

   public TelemetryDiagnosticControls(TelemetryClient telemetryClient) {
      this.telemetryClient = telemetryClient;
   }

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

      ClientConfiguration config = new ClientConfiguration();
      config.setSessionId(telemetryClient.getVersion()/*.toUpperCase()*/ + "-" + UUID.randomUUID().toString());
      config.setSessionStart(LocalDateTime.now());
      config.setAckMode(AckMode.NORMAL);
      telemetryClient.configure(config);

      telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      diagnosticInfo = telemetryClient.receive();
   }

}

class LdapUserApiClient {
   public String findUser(String a) {
      RestTemplate rest = new RestTemplate();
      return rest.getForObject("http://api.ldap.intra:9099/" + a, String.class);
   }
}

class UserService {
   private final LdapUserApiClient ldapUserApiClient;

   UserService(LdapUserApiClient ldapUserApiClient) {
      this.ldapUserApiClient = ldapUserApiClient;
   }

   public String biz() {
      //logicA
      String userLdap = ldapUserApiClient.findUser("a");
      // logic

      return userLdap.toUpperCase();
   }
}
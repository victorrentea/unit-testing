package victor.testing.mocks.telemetry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelemetryDiagnostic {
   private final TelemetryClient telemetryClient;

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

      ClientConfiguration config = createConfig(telemetryClient.getVersion());
      telemetryClient.configure(config);

      telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      diagnosticInfo = telemetryClient.receive();
   }
   // SE PARE CA:
   // daca ai o functie complexa, testata separat, simpla ei chemare
   // din codul de sus produce NPE daca nu fac when then return pe versiune
   // POLUANDU-MI testele pentru metoda de sus

   // CUM pot sa opresc asta ?
   // Adica in testele lansate pe met de sus,
   // sa NU se mai invoce metoda de jos:

   ClientConfiguration createConfig(String version) {
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
//private final MyClock myClock;
}

//@Component
//class MyClock {
//   public LocalDateTime getNow() {
//      return LocalDateTime.now();
//   }
//}
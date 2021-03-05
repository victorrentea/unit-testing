package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClientConfigurationFactory {
   public ClientConfiguration createConfiguration(String version) {
      ClientConfiguration config = new ClientConfiguration();
      config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
      config.setSessionStart(LocalDateTime.now());
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      //  MULTA LOGICA GREA . 4 ifuri si un for si un try catch
      config.setAckMode(AckMode.NORMAL);
      return config;
   }

}

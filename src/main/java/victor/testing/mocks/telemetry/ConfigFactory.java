package victor.testing.mocks.telemetry;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConfigFactory {
    //	@VisibleForTesting // cand deschizi o metoda sa fie non-privata doar pentru teste, asta crapa pe sonar
    // daca o chemi din alta parte din prod decat din teste.
    public Client.ClientConfiguration createConfig(String version) {
        Client.ClientConfiguration config = new Client.ClientConfiguration();
        config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID());
//		if for try if  ?: ai nevoie de 7 teste
        // maine devine foarte cyclomatic complex
        config.setSessionStart(LocalDateTime.now());
        config.setAckMode(Client.ClientConfiguration.AckMode.NORMAL);
        return config;
    }

}

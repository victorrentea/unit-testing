package victor.testing.mocks.telemetry;

import java.time.LocalDateTime;
import java.util.UUID;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

public class ClientConfigurationFactory {

	public ClientConfiguration createConfig(String version) {
		ClientConfiguration config = new ClientConfiguration();
		// ne inchipuim ca aici se gasesc + 20 de linii de biz logic criminal
		// ne imaginam ca vei vrea sa pui pe met asta 7 teste
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL); // <---- ASTA !
		return config;
	}
	
}

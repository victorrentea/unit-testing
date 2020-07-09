package ro.victor.unittest.mocks.telemetry;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConfigurationFactory {
	public ClientConfiguration createConfiguration(String clientVersion) {
		ClientConfiguration config = new ClientConfiguration();
		// ne imaginam ca aici e tona de logica
		config.setSessionId(clientVersion + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL); // <--- verific-o p'ASTA
		return config;
	}

}

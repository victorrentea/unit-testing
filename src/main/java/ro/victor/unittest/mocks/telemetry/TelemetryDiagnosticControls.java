package ro.victor.unittest.mocks.telemetry;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final TelemetryClient telemetryClient;
	private final ConfigFactory configFactory;

	private String diagnosticInfo = "";

	public TelemetryDiagnosticControls(TelemetryClient telemetryClient, ConfigFactory configFactory) {
		this.telemetryClient = telemetryClient;
		this.configFactory = configFactory;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}
	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
	}

	public void checkTransmission() {
		telemetryClient.disconnect();

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = configFactory.createConfig(telemetryClient.getVersion());
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}

}

class ConfigFactory {
	private final UUIDGenerator uuidGenerator;

	ConfigFactory(UUIDGenerator uuidGenerator) {
		this.uuidGenerator = uuidGenerator;
	}

	public ClientConfiguration createConfig(String version) {
		ClientConfiguration config = new ClientConfiguration();
		// Presupunem ca functia asta are 20-30 de linii in total de logica grea.
		// ->7-8 teste pe ea
		// Ca sa reusesti sa chemi aceasta functie, trebuie sa ii dai o carca
		// de parametrii complecsi (date multe, sa nu fie nule ca crapa, etc)
		config.setSessionId(version.toUpperCase() + "-" + uuidGenerator.generateRandom());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

}

class UUIDGenerator {
	public String generateRandom() {
		return UUID.randomUUID().toString();
	}
}

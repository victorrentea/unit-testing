package ro.victor.unittest.mocks.telemetry;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

// CERN
public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";
	private final TelemetryClient telemetryClient;
	private final ConfigurationFactory configFactory;
	private String diagnosticInfo = "";


	public TelemetryDiagnosticControls(TelemetryClient telemetryClient, ConfigurationFactory configFactory) {
		this.telemetryClient = telemetryClient;
		this.configFactory = configFactory;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

	public void checkTransmission() {
		telemetryClient.disconnect(); // ok

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = configFactory.createConfiguration(telemetryClient.getVersion());
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE); // ok
		diagnosticInfo = telemetryClient.receive();
	}



}

class ConfigurationFactory {
	public ClientConfiguration createConfiguration(String clientVersion) {
		ClientConfiguration config = new ClientConfiguration();
		// ne imaginam ca aici e tona de logica
		config.setSessionId(clientVersion + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL); // <--- verific-o p'ASTA
		return config;
	}

}

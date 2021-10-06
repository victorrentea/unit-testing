package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryController {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private TelemetryClient telemetryClient;
	private String diagnosticInfo = "";

	public void setTelemetryClient(TelemetryClient telemetryClient) {
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
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		// GOAL
		ClientConfiguration config = createConfig(telemetryClient.getVersion());
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}

	ClientConfiguration createConfig(String version) {
		ClientConfiguration config = new ClientConfiguration();
		//if
		//if
		//if
		//if
		//if
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

}

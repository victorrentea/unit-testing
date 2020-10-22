package victor.testing.mocks.telemetry;

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

		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(telemetryClient.getVersion()/*.toUpperCase()*/ + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL); // <---- ASTA !
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}
	

}

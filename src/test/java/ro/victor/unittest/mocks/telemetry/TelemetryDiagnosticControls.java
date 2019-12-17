package ro.victor.unittest.mocks.telemetry;

import java.time.ZoneId;
import java.util.UUID;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;
import ro.victor.unittest.time.rule.TimeProvider;

public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private TelemetryClient telemetryClient;
	private String diagnosticInfo = "";

//	public TelemetryDiagnosticControls setTelemetryClient(TelemetryClient telemetryClient) {
//		this.telemetryClient = telemetryClient;
//		return this;
//	}

	public TelemetryDiagnosticControls(TelemetryClient telemetryClient) {
		this.telemetryClient = telemetryClient;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}
	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
	}

	public void checkTransmission() throws Exception {
		telemetryClient.disconnect(); // asta

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}
		

		if (! telemetryClient.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect."); // OK
		}

		telemetryClient.configure(createClientConfiguration()); // ??

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE); // OK
		diagnosticInfo = telemetryClient.receive(); // OK
	}

	ClientConfiguration createClientConfiguration() {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(UUID.randomUUID().toString());
		config.setSessionStart(java.util.Date.from(TimeProvider.currentDate().atStartOfDay()
				.atZone(ZoneId.systemDefault())
				.toInstant()).getTime());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

}

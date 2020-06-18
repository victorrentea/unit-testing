package ro.victor.unittest.mocks;

import org.springframework.beans.factory.annotation.Autowired;

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

//		int currentRetry = 1;
//		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
//			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
//			currentRetry ++;
//		}

		if (! telemetryClient.getOnlineStatus() && f()) {
			throw new IllegalStateException("Unable to connect.");
		}

		telemetryClient.configure(createConfiguration());

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}

	private TelemetryClient.ClientConfiguration createConfiguration() {
		TelemetryClient.ClientConfiguration config = new TelemetryClient.ClientConfiguration();
		config.setSessionId(telemetryClient.getVersion() + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(TelemetryClient.ClientConfiguration.AckMode.NORMAL);
		return config;
	}
	// +getter ar permite asert direct pe ce s-a calculat
	// DAR, murdareste designul de prod pentru folosul strict al testelor
	// pt ca probabil se va pastra o ref la "config" si din Client oricum.
//	private TelemetryClient.ClientConfiguration config;

	private boolean f() {
		return true;
	}

}

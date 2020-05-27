package ro.victor.unittest.mocks.telemetry;

import java.time.LocalDateTime;
import java.util.UUID;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static ro.victor.unittest.mocks.telemetry.TelemetryException.ErrorCode.NOT_CONNECTED;
import static ro.victor.unittest.mocks.telemetry.TelemetryException.ErrorCode.VALUE_IS_1;

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

	public void checkTransmission(int value) {

//		try {
			telemetryClient.disconnect();
//		} catch (TelemetryExceptionHandledException e) {
			// Recomandare: creati subtipuri de exceptii doar pentru cazurile cand le si tratati selectiv
//		}

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()/* || true*/) {
			throw new TelemetryException("Unable to connect.", NOT_CONNECTED);
		}

		if (value == 1) {
			throw new TelemetryException("value is 1", VALUE_IS_1);
		}
		// cum testam astea:
		ClientConfiguration config = createConfig();
		telemetryClient.configure(config);
		// pana aici

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}

	ClientConfiguration createConfig() {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(telemetryClient.getVersion() + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

}

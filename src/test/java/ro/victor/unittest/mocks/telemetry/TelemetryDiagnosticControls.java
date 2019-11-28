package ro.victor.unittest.mocks.telemetry;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private TelemetryClient telemetryClient;
	private final Clock clock;
	private String diagnosticInfo = "";

	public TelemetryDiagnosticControls(TelemetryClient telemetryClient, Clock clock) {
		this.telemetryClient = telemetryClient;
        this.clock = clock;
    }

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}
	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
	}

	public void checkTransmission() throws Exception {
		telemetryClient.disconnect(); // OK

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new Exception("Unable to connect."); // OK
		}

		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(UUID.randomUUID().toString());
		config.setSessionStart(java.util.Date
                .from(LocalDateTime.now(clock)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()).getTime());
		config.setAckMode(AckMode.NORMAL);

		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE); // OK
		diagnosticInfo = telemetryClient.receive(); // OK
	}

}

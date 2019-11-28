package ro.victor.unittest.mocks.telemetry;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static java.util.Date.*;

public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final TelemetryClient telemetryClient;
	private final ClientConfigurationFactory configFactory;

	public TelemetryDiagnosticControls(TelemetryClient telemetryClient, ClientConfigurationFactory configFactory) {
		this.telemetryClient = telemetryClient;
		this.configFactory = configFactory;
	}

	private String diagnosticInfo = "";

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

	public void checkTransmission() throws IllegalArgumentException {
		telemetryClient.disconnect(); // OK

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new IllegalArgumentException("Unable to connect."); // OK
		}

		telemetryClient.configure(configFactory.createConfig());

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE); // OK
		diagnosticInfo = telemetryClient.receive(); // OK
	}

}


//mai low level ca cea de sus
//@Service
class ClientConfigurationFactory {
	private final Clock clock;

	public ClientConfigurationFactory(Clock clock) {
		this.clock = clock;
	}

	public ClientConfiguration createConfig() {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(UUID.randomUUID().toString());
		config.setSessionStart(from(LocalDateTime.now(clock).atZone(ZoneId.systemDefault()).toInstant()).getTime());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}
}

package victor.testing.mocks.telemetry;


import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	@Inject
	private TelemetryClient telemetryClient;
	@Inject
	private ClientConfigurationFactory factory;

	public void setFactory(ClientConfigurationFactory factory) {
		this.factory = factory;
	}

	public void setTelemetryClient(TelemetryClient telemetryClient) {
		this.telemetryClient = telemetryClient;
	}

	private String diagnosticInfo = "";

	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

	public void checkTransmission(boolean force) {
		telemetryClient.disconnect(force); // THIS LINE

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = factory.createConfig(telemetryClient.getVersion());
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}


}

class ClientConfigurationFactory {

	public ClientConfiguration createConfig(String version) {
		ClientConfiguration config = new ClientConfiguration();
		// NO HEAVY LOGIC !! :)
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}
}


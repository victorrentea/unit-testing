package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

// cod scris de altu pe care tu curand va trebui sa il modifici dramatic.
// sau cod pe care ai gasit 5 buguri in ultimele 6 luni
// sau cod schimbat lunar.
public class TelemetryDiagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final TelemetryClient telemetryClient;
	private final ClientConfigurationFactory clientConfigurationFactory;
	private String diagnosticInfo = "";

	public TelemetryDiagnostic(TelemetryClient telemetryClient, ClientConfigurationFactory clientConfigurationFactory) {
		this.telemetryClient = telemetryClient;
		this.clientConfigurationFactory = clientConfigurationFactory;
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
		while (!telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry++;
		}

		if (!telemetryClient.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = clientConfigurationFactory.createConfig(
				telemetryClient.getVersion());
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}

}
class ClientConfigurationFactory {
	public ClientConfiguration createConfig(String version) { // tre sa pun 7 teste pe asta
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		// assume a lot of cyclomatic complexity 7 ifuri
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

}

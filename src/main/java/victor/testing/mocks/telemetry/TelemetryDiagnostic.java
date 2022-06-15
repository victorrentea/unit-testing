package victor.testing.mocks.telemetry;

import com.google.common.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;
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
	private String diagnosticInfo = "";

	public TelemetryDiagnostic(TelemetryClient telemetryClient) {
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

		ClientConfiguration config = createConfig();
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}

	@VisibleForTesting
	ClientConfiguration createConfig() { // tre sa pun 7 teste pe asta
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(telemetryClient.getVersion().toUpperCase() + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		// assume a lot of cyclomatic complexity 7 ifuri
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

}

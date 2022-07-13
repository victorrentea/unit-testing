package victor.testing.mocks.telemetry;

import com.google.common.annotations.VisibleForTesting;
import org.apache.kafka.server.authorizer.Authorizer;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryDiagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private TelemetryClient telemetryClient;
	private String diagnosticInfo = "";
Authorizer authorizer;
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

		ClientConfiguration config = configureClient(telemetryClient.getVersion());
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}

	@VisibleForTesting
	public ClientConfiguration configureClient(String version) {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(
				version/*.toUpperCase()*/ +
				"-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		// 10 ifs to determine what to put in config.
		config.setAckMode(AckMode.NORMAL);
		return config;

	}
}

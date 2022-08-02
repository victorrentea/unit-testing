package victor.testing.mocks.telemetry;

import com.google.common.annotations.VisibleForTesting;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final Client clientMock;
	private String diagnosticInfo = "";

	public Diagnostic(Client clientMock) {
		this.clientMock = clientMock;
	}

	public void checkTransmission(boolean force) {
		clientMock.disconnect(force);

		int currentRetry = 1;
		while (! clientMock.getOnlineStatus() && currentRetry <= 3) {
			clientMock.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! clientMock.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = configureClient(clientMock.getVersion());
		clientMock.configure(config);

		clientMock.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = clientMock.receive();
	}

	@VisibleForTesting
	public ClientConfiguration configureClient(String version) { // are nevoie de 7 teste
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}
}

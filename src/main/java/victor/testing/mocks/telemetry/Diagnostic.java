package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private Client clientMock;
	private String diagnosticInfo = "";

	public void setTelemetryClient(Client client) {
		this.clientMock = client;
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

		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(clientMock.getVersion()/*.toUpperCase()*/ + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		clientMock.configure(config);

		clientMock.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = clientMock.receive();
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

}

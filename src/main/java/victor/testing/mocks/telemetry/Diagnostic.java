package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private Client client;
	private String diagnosticInfo = "";

	public void setTelemetryClient(Client client) {
		this.client = client;
	}

	public void checkTransmission(boolean force) {
		client.disconnect(force);

		if (! client.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		configureClient();

		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
	}

	private void configureClient() {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(client.getVersion().toUpperCase() + "-" + randomUUID());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		// + 7 ifs are here !!! => + 7 tests (or @ParameterizedTest🤞)
		client.configure(config);
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

}

// CR: the version concatenated into the sessionID needs to be UPPERCASED
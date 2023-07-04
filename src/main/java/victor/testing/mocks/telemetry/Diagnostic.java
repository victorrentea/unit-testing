package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.Clock;
import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";
	private final Client client;
	private String diagnosticInfo = "";

	public Diagnostic(Client client) {
		this.client = client;
	}

	public void checkTransmission(boolean force) {
		client.disconnect(force);

		if (! client.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(client.getVersion() + "-" +
				/*randomFactory.*/randomUUID());
		config.setSessionStart(LocalDateTime.now(/*clock*/));
		config.setAckMode(AckMode.NORMAL);
		client.configure(config);

		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
		// ce dob*toc ar face asta ?!!
	}
//	Clock clock; // injectat de spring, respectiv un @Mock in teste sau un Clock.fixed()

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

}

package victor.testing.mocks.telemetry;

import org.assertj.core.util.VisibleForTesting;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

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

		int currentRetry = 1;
		while (! client.getOnlineStatus() && currentRetry <= 3) {
			client.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! client.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = createConfig(client.getVersion());
		client.configure(config);

		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
	}

	@VisibleForTesting
	ClientConfiguration createConfig(String clientVersion) { // imagine 12 tests written on this method
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(clientVersion + "-" + randomUUID());
		config.setSessionStart(LocalDateTime.now());
		// 😱😱😱😱😱😱😱😱😱😱imagine terrible complexity accumulates here !!! 12
		// cyclomatic complexity = 1 + number of if/else + number of loops ~= a max of how many tests you have to write
		config.setAckMode(AckMode.NORMAL);
		return config;

	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

//	public boolean receivedCorrectDIagnosticMessage() {
//		return diagnosticInfo.equals("Something");
//	}

}

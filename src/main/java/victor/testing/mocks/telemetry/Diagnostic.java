package victor.testing.mocks.telemetry;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.Clock;
import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";
//	private final TimeProvider clock; // interfata dedicata
//	private final Clock clock; // + clock.millis in prod
	private final Client client;
	private final UUIDGenerator uuidGenerator;

	private String diagnosticInfo = "";

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
		ClientConfiguration config = createConfig(force);
		client.configure(config);
		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
	}

	@VisibleForTesting // doar apelabila din src/test
	ClientConfiguration createConfig(boolean force) {
		ClientConfiguration config = new ClientConfiguration();
		// inchipuie aici + 7 ifuri
		config.setSessionId(client.getVersion().toUpperCase() + "-" + uuidGenerator.uuid());
		config.setSessionStart(LocalDateTime.now());
		if (!force)
			config.setAckMode(AckMode.NORMAL);
		else
			config.setAckMode(AckMode.FLOOD);
		return config;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

}

package victor.testing.mocks.telemetry;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
@Component
public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";
	private final Client client;
	private String diagnosticInfo = "";

	// BAD
//	private ClientConfiguration config;
//	public ClientConfiguration getConfig() {
//		return config;
//	}

	public void checkTransmission(boolean force) {
		client.disconnect(force);

		int currentRetry = 1;
		while (! client.getOnlineStatus() && currentRetry <= 3) {
			client.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! client.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
//			throw new MyException(ErrorCode.UNABLE_TO_CONNECT); // rendered to the user use an enum in your own exception
		}

		ClientConfiguration config = createConfig();
		client.configure(config);

		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
//		return config; // least harmful, but still a change in prod API, violating CQS principle
	}


	// option1: subcutaneous test (@VisibleForTesting)
	// option2: refactor more > break the class!! [HARD but correct]
	@VisibleForTesting
	ClientConfiguration createConfig() {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(client.getVersion().toUpperCase() + "-" + randomUUID());
		// please imagine this block gets a Cyclomatic Complexity of 12
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

//	private final ConfigFactory configFactory;
}

// BAD just for testing :over enginerreing
//class ConfigFactory {
//	public ClientConfiguration newInstance() {
//		return new ClientConfiguration();
//	}
//}
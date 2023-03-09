package victor.testing.mocks.telemetry;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;
import scala.concurrent.impl.FutureConvertersImpl.CF;
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

		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(client.getVersion()/*.toUpperCase()*/ + "-" + randomUUID());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		client.configure(config);

		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
//		return config; // least harmful, but still a change in prod API, violating CQS principle
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
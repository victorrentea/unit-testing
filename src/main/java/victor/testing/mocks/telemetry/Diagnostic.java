package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final Client clientMock;
	private final ClientConfigurationFactory clientConfigurationFactory;
	private String diagnosticInfo = "";

	public Diagnostic(Client clientMock, ClientConfigurationFactory clientConfigurationFactory) {
		this.clientMock = clientMock;
		this.clientConfigurationFactory = clientConfigurationFactory;
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

		ClientConfiguration config = clientConfigurationFactory.createConfig(clientMock.getVersion());
		clientMock.configure(config);

		clientMock.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = clientMock.receive();
	}

	// this is NOT as bad as:
	// adding an extra param, extra field, exposing a public field
	// just for testing


	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

}

class ClientConfigurationFactory {
	public	ClientConfiguration createConfig(String version) {
		ClientConfiguration config = new ClientConfiguration();
		// imagine dragons/ 17 ifs. BR that no one understands
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}
}

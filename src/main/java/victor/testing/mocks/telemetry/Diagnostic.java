package victor.testing.mocks.telemetry;

import com.google.common.annotations.VisibleForTesting;
import lombok.NonNull;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

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

		var config = createConfig();
		client.configure(config);

		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
	}

	@VisibleForTesting // OR if complex enough, extract this in a separate class
	@NonNull ClientConfiguration createConfig() {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(client.getVersion()/*.toUpperCase()*/ + "-" + randomUUID());
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		// a lot of complexity
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

}

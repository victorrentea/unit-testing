package victor.testing.mocks.telemetry;

import com.google.common.annotations.VisibleForTesting;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.InstantSource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";
  public static final ZoneId ZONE_ID = ZoneId.systemDefault();

  private final Client client;
	private String diagnosticInfo = "";
	private final UUIDFactoryInterface uuidFactory;
	private final InstantSource instantSource;


	public Diagnostic(Client client, UUIDFactoryInterface uuidFactory, InstantSource instantSource) {
		this.client = client;
		this.uuidFactory = uuidFactory;
		this.instantSource = instantSource;
	}

	public void checkTransmission(boolean force) {
		client.disconnect(force);
		if (! client.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}
		ClientConfiguration config = configureClient(client.getVersion());
		client.configure(config);
		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
	}
	@VisibleForTesting //less encapsulated but protected by tools like Sonar
	/*private */ ClientConfiguration configureClient(String version) {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}
	// CR: the version concatenated to the sessionId should be uppercased

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

}

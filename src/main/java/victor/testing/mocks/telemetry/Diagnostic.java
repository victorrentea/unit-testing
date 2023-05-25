package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.InstantSource;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.util.UUID.randomUUID;

public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";
  public static final ZoneId ZONE_ID = ZoneId.systemDefault();

  private final Client client;
	private String diagnosticInfo = "";
	private final UUIDFactory uuidFactory;
	private final InstantSource instantSource;


	public Diagnostic(Client client, UUIDFactory uuidFactory, InstantSource instantSource) {
		this.client = client;
		this.uuidFactory = uuidFactory;
		this.instantSource = instantSource;
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

		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(client.getVersion() +
//												"-" + randomUUID());
												"-" + uuidFactory.get());
//		LocalDateTime now = LocalDateTime.ofInstant(instantSource.instant(), ZONE_ID);
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		client.configure(config);


		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

}

package victor.testing.mocks.telemetry;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Diagnostic {
	private Client client;
	@Getter
	private String diagnosticInfo = "";

	public void setTelemetryClient(Client client) {
		this.client = client;
	}

	public void checkTransmission(boolean force) {
		client.disconnect(force);

		System.out.println("Ora curenta este : " + LocalDateTime.now());
		if (! client.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(client.getVersion()/*.toUpperCase()*/ + "-" + randomUUID());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		client.configure(config);

		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
	}

}



package victor.testing.mocks.telemetry;

import com.google.common.annotations.VisibleForTesting;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

// am gasit acest cod prin proiect.
// si am ajuns la concl ca trebuie sa-l schimb

public class Diagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";
	private final Client client;

	public Diagnostic(Client client) {
		this.client = client;
	}

	private String diagnosticInfo = "";

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}
	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
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

		ClientConfiguration config = parteaComplexa(client.getVersion());
		client.configure(config);

		client.send(Client.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = client.receive();
	}

	@VisibleForTesting // cand deschizi o metoda sa fie non-privata doar pentru teste, asta crapa pe sonar
	// daca o chemi din alta parte din prod decat din teste.
	ClientConfiguration parteaComplexa(String version) {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID());
//		if for try if  ?: ai nevoie de 7 teste
		// maine devine foarte cyclomatic complex
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

}

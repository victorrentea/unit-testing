package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryDiagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final TelemetryClient telemetryClient;
	private final ConfigurationFactory configurationFactory;
	private String diagnosticInfo = "";

	public TelemetryDiagnostic(TelemetryClient telemetryClient, ConfigurationFactory configurationFactory) {
		this.telemetryClient = telemetryClient;
		this.configurationFactory = configurationFactory;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}
	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
	}

	// ai adaugat stare long-lived DOAR PENTRU TESTE
	// = test-induced design damage.
//	ClientConfiguration config; // CAMP NU!

	public void checkTransmission(boolean force) {
		telemetryClient.disconnect(force);

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = configurationFactory.createConfigComplexa(telemetryClient.getVersion());
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE, LocalDateTime.now()); // asta
		diagnosticInfo = telemetryClient.receive();
	}

}


class ConfigurationFactory {
	public ClientConfiguration createConfigComplexa(String version) {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		// logica complexa cu cyclomatic complexity =20 ~= 20 de teste trebe aci
		config.setAckMode(AckMode.NORMAL); // ASTA testam dupa pauza
		return config;
	}

}
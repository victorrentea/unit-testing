package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryDiagnostic {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final TelemetryClient telemetryClient;
	private String diagnosticInfo = "";

	public TelemetryDiagnostic(TelemetryClient telemetryClient) {
		this.telemetryClient = telemetryClient;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}
	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
	}

	// XXX: ASTA O TESTEZ
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

		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(telemetryClient.getVersion() + "-" + UUID.randomUUID());
		config.setSessionStart(LocalDateTime.now());
		/* logica complexa cu cyclomatic complexity =20 ~= 20 de teste trebe aci */
		config.setAckMode(AckMode.NORMAL); // ASTA testam dupa pauza
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE, LocalDateTime.now()); // asta
		diagnosticInfo = telemetryClient.receive();
	}
	// DAMAGE de design : ai incarcat cu state obiectul asta DOAR PT TESTE
	// cu pbolemele de rigoare: cureti starea, multi threading, memory
//	private String p;
//	public String getP() {
//		return p;
//	}
}
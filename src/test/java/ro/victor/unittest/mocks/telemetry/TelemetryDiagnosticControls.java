package ro.victor.unittest.mocks.telemetry;

import java.time.LocalDateTime;
import java.util.UUID;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final TelemetryClient telemetryClient;
	private final ClientConfigurationProvider configurationProvider;
	private String diagnosticInfo = "";
	private boolean soarelePeCer;
	private boolean lunaPeCer;

	public TelemetryDiagnosticControls(TelemetryClient telemetryClient, ClientConfigurationProvider configurationProvider) {
		this.telemetryClient = telemetryClient;
		this.configurationProvider = configurationProvider;
	}


	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}
	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
	}

	public void checkTransmission() throws Exception {
		telemetryClient.disconnect(); // OK

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new SGException(SGException.ErrorCode.UNABLE_TO_CONNECT); // OK
		}

		ClientConfiguration config = configurationProvider.createConfig();
		telemetryClient.configure(config);


		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE); // OK
		diagnosticInfo = telemetryClient.receive();
	}

}

class ClientConfigurationProvider {
	public ClientConfiguration createConfig() {
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(UUID.randomUUID().toString());
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL);
		return config;
	}

}

class SGException extends RuntimeException {
	enum ErrorCode {
		GENERAL,
		SOARELE,
		LUNA,
		UNABLE_TO_CONNECT
	}
	private final ErrorCode code;

	public ErrorCode getCode() {
		return code;
	}

	public SGException(ErrorCode code) {
		this.code = code;
	}

	public SGException(String message, ErrorCode code) {
		super(message);
		this.code = code;
	}

	public SGException(String message, Throwable cause, ErrorCode code) {
		super(message, cause);
		this.code = code;
	}

	public SGException(Throwable cause, ErrorCode code) {
		super(cause);
		this.code = code;
	}

	public SGException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode code) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.code = code;
	}
}


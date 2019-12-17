package ro.victor.unittest.mocks.telemetry;

import java.time.ZoneId;
import java.util.UUID;

import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;
import ro.victor.unittest.time.TimeProvider;

public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final TelemetryClient telemetryClient;
	private String diagnosticInfo = "";
	private boolean soarelePeCer;
	private boolean lunaPeCer;

	public TelemetryDiagnosticControls(TelemetryClient telemetryClient) {
		this.telemetryClient = telemetryClient;
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

		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(UUID.randomUUID().toString());
		config.setSessionStart(java.util.Date.from(TimeProvider.currentDate().atStartOfDay()
				.atZone(ZoneId.systemDefault())
				.toInstant()).getTime());
		config.setAckMode(AckMode.NORMAL);
		telemetryClient.configure(config);


		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE); // OK
		diagnosticInfo = telemetryClient.receive();
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


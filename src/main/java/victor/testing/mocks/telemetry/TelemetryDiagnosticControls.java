package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final  ConfigurationFactory configurationFactory;
	private final TelemetryClient telemetryClient;
	private String diagnosticInfo = "";


	public TelemetryDiagnosticControls(ConfigurationFactory configurationFactory, TelemetryClient telemetryClient) {
		this.configurationFactory = configurationFactory;
		this.telemetryClient = telemetryClient;
	}

	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}

	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
	}
	public void checkTransmission() {
		telemetryClient.disconnect();

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
		}

		ClientConfiguration config = configurationFactory.createConfig(telemetryClient.getVersion());
		telemetryClient.configure(config);

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();  // 1sec
	}
}



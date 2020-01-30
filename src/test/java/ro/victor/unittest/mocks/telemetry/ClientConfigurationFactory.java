package ro.victor.unittest.mocks.telemetry;

import ro.victor.unittest.time.rule.TimeProvider;

import java.time.ZoneId;
import java.util.UUID;

public class ClientConfigurationFactory {
	public TelemetryClient.ClientConfiguration createConfig() {
		TelemetryClient.ClientConfiguration config = new TelemetryClient.ClientConfiguration();
		config.setSessionId(UUID.randomUUID().toString());
		config.setSessionStart(java.util.Date.from(TimeProvider.currentDate().atStartOfDay()
				.atZone(ZoneId.systemDefault())
				.toInstant()).getTime());
		config.setAckMode(TelemetryClient.ClientConfiguration.AckMode.NORMAL);
		return config;
	}

}

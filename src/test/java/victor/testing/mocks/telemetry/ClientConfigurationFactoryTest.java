package victor.testing.mocks.telemetry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

class ClientConfigurationFactoryTest {
	
	private ClientConfigurationFactory factory = new ClientConfigurationFactory();
	@Test
	public void configuresClient() throws Exception {
		ClientConfiguration config = factory.createConfig("ver");
		assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
		assertThat(config.getSessionId()).startsWith("VER-");
		assertThat(config.getSessionStart()).isNotNull(); // engineering
	}
	
}
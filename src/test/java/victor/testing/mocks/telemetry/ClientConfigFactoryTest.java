package victor.testing.mocks.telemetry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

public class ClientConfigFactoryTest {
	@Test
	public void createsConfig() throws Exception { // x 7 teste
		ClientConfiguration config = new ClientConfigurationFactory().createConfig("ver");
		
		assertEquals(AckMode.NORMAL, config.getAckMode());
		assertThat(config.getSessionId()).matches("VER-.+");
	}
	// + 20 teste ca e multa logica acolo (ne imaginam)
	
}

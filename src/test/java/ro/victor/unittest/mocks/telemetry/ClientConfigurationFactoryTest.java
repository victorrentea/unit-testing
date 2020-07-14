package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;

// schita, eseu:
public class ClientConfigurationFactoryTest {
    @Test
    public void configuresClient2() {
        ClientConfiguration config = new ClientConfigurationFactory().createConfig("VERS");
        assertThat(config.getAckMode()).isEqualTo(AckMode.FLOOD); // mai brain-friend
        assertThat(config.getSessionId()).isNotNull();
        assertThat(config.getSessionId()).startsWith("VERS-");
    }

}

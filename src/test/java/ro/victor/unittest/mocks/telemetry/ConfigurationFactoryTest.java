package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationFactoryTest {
    @Test
    public void createsConfiguration() {
        ConfigurationFactory victim = new ConfigurationFactory();
        ClientConfiguration config = victim.createConfiguration("VersionNo");
        assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);

        assertThat(config.getSessionId()).isNotNull();

        assertThat(config.getSessionId()).startsWith("VersionNo-");
    }
}

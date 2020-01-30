package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientConfigurationFactoryShould {

    @Test
    public void createConfig() throws Exception {
        TelemetryClient.ClientConfiguration config = new ClientConfigurationFactory().createConfig();
        assertThat(config.getAckMode()).isEqualTo(TelemetryClient.ClientConfiguration.AckMode.NORMAL);
        assertThat(new Date(config.getSessionStart())).isInSameDayAs(new Date());
        assertThat(config.getSessionId()).isNotNull();
    }
}

package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static java.util.Date.from;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ClientConfigurationFactoryTest {
    private Clock clock = Clock.fixed(LocalDateTime.parse("2019-01-01T12:12:12").toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

    @Test
    public void createsConfigOk() throws Exception {
        ClientConfigurationFactory factory = new ClientConfigurationFactory(clock);
        TelemetryClient.ClientConfiguration configDinProd =factory.createConfig();
        assertThat(configDinProd.getAckMode()).isEqualTo(TelemetryClient.ClientConfiguration.AckMode.NORMAL);
        assertThat(configDinProd.getSessionStart()).isEqualTo(from(LocalDateTime.now(clock).atZone(ZoneId.systemDefault()).toInstant()).getTime());
        assertThat(configDinProd.getSessionId()).isNotNull();
    }
}

package ro.victor.unittest.mocks.telemetry;

import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.Test;

import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClientConfigurationProviderTest {
    @Test
    public void createsCorrectConfig() {
        TelemetryClient.ClientConfiguration config = new ClientConfigurationProvider().createConfig();
        assertEquals(TelemetryClient.ClientConfiguration.AckMode.NORMAL, config.getAckMode());
        assertNotNull(config.getSessionId());
        assertThat(config.getSessionStart()).isEqualToIgnoringMinutes(now());
        assertThat(config.getSessionStart()).isCloseTo(now(), new TemporalUnitWithinOffset(1, ChronoUnit.MINUTES));
    }
}

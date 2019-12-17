package ro.victor.unittest.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.TemporalOffset;
import org.assertj.core.data.TemporalUnitOffset;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.SGException.ErrorCode;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsShould {

    @Mock
    private TelemetryClient telemetryClient;
    @InjectMocks
    private TelemetryDiagnosticControls controls;

    @Before
    public void tomberon() {
    }
    @Test
    public void disconnects() throws Exception {
        when(telemetryClient.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission();
        verify(telemetryClient).disconnect();
    }
    @Test
    public void sends() throws Exception {
        when(telemetryClient.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission();
        verify(telemetryClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
    }

//    @Test(expected = IllegalStateException.class)
//    public void throwsWhenOnlineStatusIsFalse() throws Exception {
//        when(telemetryClient.getOnlineStatus()).thenReturn(false);
//        controls.checkTransmission();
//    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void throwsWhenOnlineStatusIsFalse_checkingExceptionMessage() throws Exception {
        when(telemetryClient.getOnlineStatus()).thenReturn(false);
//        exception.expect(IllegalStateException.class);
//        exception.expectMessage("Unable to connect.");
        exception.expect(new SGExceptionMatcher(ErrorCode.UNABLE_TO_CONNECT));
        controls.checkTransmission();
    }
    @Test
    public void receives() throws Exception {
        when(telemetryClient.getOnlineStatus()).thenReturn(true);
        when(telemetryClient.receive()).thenReturn("tataie");
        controls.checkTransmission();
        assertEquals("tataie", controls.getDiagnosticInfo());
//        verify(telemetryClient).receive(); // inutil: niciodata nu crapa doar di ncauza asta.
    }

    @Test
    public void configuresClient() throws Exception {
        when(telemetryClient.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission();
        ArgumentCaptor<ClientConfiguration> configCapture =
                forClass(ClientConfiguration.class);
        verify(telemetryClient).configure(configCapture.capture());
        ClientConfiguration config = configCapture.getValue();
        assertEquals(AckMode.NORMAL, config.getAckMode());
        assertNotNull(config.getSessionId());
        assertThat(config.getSessionStart()).isEqualToIgnoringMinutes(now());
        assertThat(config.getSessionStart()).isCloseTo(now(), new TemporalUnitWithinOffset(1, ChronoUnit.MINUTES));
    }
    // TO BE CONTINUED....

    @Test
    public void createsCorrectConfig() {
        ClientConfiguration config = controls.createConfig();
        assertEquals(AckMode.NORMAL, config.getAckMode());
        assertNotNull(config.getSessionId());
        assertThat(config.getSessionStart()).isEqualToIgnoringMinutes(now());
        assertThat(config.getSessionStart()).isCloseTo(now(), new TemporalUnitWithinOffset(1, ChronoUnit.MINUTES));
    }

}

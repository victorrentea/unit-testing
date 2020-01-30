package ro.victor.unittest.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.time.rule.TestTimeRule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
    @Mock
    private TelemetryClient client;
    @InjectMocks
    private TelemetryDiagnosticControls controls;

    @Before
    public void initialize() {
        when(client.getOnlineStatus()).thenReturn(true);
    }
    @Test
    public void disconnects() throws Exception {
        controls.checkTransmission();
        verify(client).disconnect();
    }
    @Test(expected = IllegalStateException.class)
    public void throwsWhenNotOnline() throws Exception {
        when(client.getOnlineStatus()).thenReturn(false); // !!!!!!!
        controls.checkTransmission();
    }
    @Test
    public void sendsDiagnosticInfo() throws Exception {
        controls.checkTransmission();
        verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
    }
    @Test
    public void receivesDiagnosticInfo() throws Exception {
        when(client.receive()).thenReturn("tataie");
        controls.checkTransmission();
//        verify(client).receive(); useless: never crashes the test alone.
        // sa verifici metode pe care le faci when.thenReturn NU ARE SENS
        assertThat(controls.getDiagnosticInfo()).isEqualTo("tataie");
    }
    @Test
    public void configuresClient() throws Exception {
        controls.checkTransmission();
        verify(client).configure(notNull());
    }

    @Test
    public void configuresClientFaraCaptor() throws Exception {
        ClientConfiguration config = new TelemetryDiagnosticControls(null).createConfig();
        assertThat(config.getAckMode()).isEqualTo(ClientConfiguration.AckMode.NORMAL);
        assertThat(new Date(config.getSessionStart())).isInSameDayAs(new Date());
        assertThat(config.getSessionId()).isNotNull();
    }

}

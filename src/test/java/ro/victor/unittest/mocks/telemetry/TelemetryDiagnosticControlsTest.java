package ro.victor.unittest.mocks.telemetry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.time.TestTimeRule;
import ro.victor.unittest.time.TimeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
    @Mock
    private TelemetryClient client;

    @InjectMocks
    private TelemetryDiagnosticControls controls;

    @Before
    public void prepareMocks() {
        when(client.getOnlineStatus()).thenReturn(true);
    }

    @Test
    public void disconnectsFromClient() throws Exception {
        controls.checkTransmission();
        verify(client).disconnect();
    }
    @Test
    public void faceSend() throws Exception {
        controls.checkTransmission();
        verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
    }

    @Test(expected = IllegalStateException.class)
    public void throwWhenOffline() throws Exception {
        when(client.getOnlineStatus()).thenReturn(false);
        controls.checkTransmission();
    }
    @Test
    public void diagnosticInfoIsReceivedFromClient() throws Exception {
        when(client.receive()).thenReturn("tataie");
        controls.checkTransmission();
        assertEquals("tataie",controls.getDiagnosticInfo());
//        verify(client).receive(); // redundant
    }
    @Test
    public void configuresClient() throws Exception {
        ArgumentCaptor<ClientConfiguration> captor = ArgumentCaptor.forClass(ClientConfiguration.class);
        controls.checkTransmission();
        verify(client).configure(captor.capture());

        ClientConfiguration config = captor.getValue();
        assertEquals(ClientConfiguration.AckMode.NORMAL, config.getAckMode());
    }

    @Rule
    public TestTimeRule rule = new TestTimeRule(
            LocalDateTime.of(LocalDate.parse("2019-01-01"), LocalTime.MIDNIGHT));

    @Test
    public void createClientConfiguration() {
        ClientConfiguration config = controls.createClientConfiguration();
        assertEquals(ClientConfiguration.AckMode.NORMAL, config.getAckMode());
        assertEquals(java.util.Date.from(rule.getTestTime()
                .atZone(ZoneId.systemDefault())
                .toInstant()).getTime(), config.getSessionStart());
    }

}

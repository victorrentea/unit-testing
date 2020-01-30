package ro.victor.unittest.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
    @Mock
    private TelemetryClient client;
    @Mock
    private ClientConfigurationFactory configurationFactory;
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
        verify(client, times(2)).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
    }
    @Test
    public void receivesDiagnosticInfo() throws Exception {
        when(client.receive()).thenReturn("tataie","mamaie");
        controls.checkTransmission();
//        verify(client).receive(); useless: never crashes the test alone.
        // sa verifici metode pe care le faci when.thenReturn NU ARE SENS
        assertThat(controls.getDiagnosticInfo()).isEqualTo("tataie");
    }
    @Test
    public void configuresClient() throws Exception {
        when(configurationFactory.createConfig()).thenReturn(new ClientConfiguration());
        controls.checkTransmission();
        verify(client).configure(notNull());
    }


}

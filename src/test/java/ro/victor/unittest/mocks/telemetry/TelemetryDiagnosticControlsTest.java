package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
    @Mock
    private TelemetryClient client;
    @InjectMocks
    private TelemetryDiagnosticControls controls;

    @Test
    public void disconnects() {
        // Intentia default ar trebui sa fie sa testtezi si clientul in joc cu Controls, eventual decupland sub client, cat mai aproape de exterior.
        when(client.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission();
        verify(client).disconnect();
    }
    @Test(expected = IllegalStateException.class)
    public void throwsWhenNotOnline() {
        when(client.getOnlineStatus()).thenReturn(false);
        controls.checkTransmission();
    }

}

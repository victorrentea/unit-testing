package ro.victor.unittest.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.TelemetryClient;
import ro.victor.unittest.mocks.TelemetryDiagnosticControls;
import ro.victor.unittest.mocks.X;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
    @Mock
    private TelemetryClient mockClient;
    @InjectMocks
    private TelemetryDiagnosticControls controls;

    @Test
    public void ok() {
        when(mockClient.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission();
    }

    @Test(expected = IllegalStateException.class)
    public void bum() {
        when(mockClient.getOnlineStatus()).thenReturn(false);
        controls.checkTransmission();
    }

}

package ro.victor.unittest.mocks.telemetry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.SGException.ErrorCode;

import static org.junit.Assert.assertEquals;
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

}

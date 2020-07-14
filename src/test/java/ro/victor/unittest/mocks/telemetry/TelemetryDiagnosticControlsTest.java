package ro.victor.unittest.mocks.telemetry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.omg.CORBA.TCKind;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.time.rule.TestTimeRule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TelemetryDiagnosticControlsTest {

    @Test
    public void test() {
        TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls();
        TelemetryClient clientMock = mock(TelemetryClient.class);
        controls.setTelemetryClient(clientMock);

        when(clientMock.getOnlineStatus()).thenReturn(true);

        controls.checkTransmission();

        verify(clientMock).disconnect();
    }

    @Test(expected = IllegalStateException.class)
    public void throwsWhenOffline() {
        TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls();
        TelemetryClient clientMock = mock(TelemetryClient.class);
        controls.setTelemetryClient(clientMock);

        when(clientMock.getOnlineStatus()).thenReturn(false);

        controls.checkTransmission();
    }

}

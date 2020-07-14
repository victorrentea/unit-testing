package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TelemetryDiagnosticControls_CheckTransmissionTest {

    @Test
    public void disconnectsAndSends() {
        TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls();
        TelemetryClient clientMock = mock(TelemetryClient.class);
        controls.setTelemetryClient(clientMock);

        when(clientMock.getOnlineStatus()).thenReturn(true);

        controls.checkTransmission();

        verify(clientMock).disconnect();

        verify(clientMock).send(anyString()); // orice string e bun -> error prome

        verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // a - mai mereu

        verify(clientMock).send("AT#UD"); // b -- cand stringul e parte dintr-un protocol de comm cu un sistem extern

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

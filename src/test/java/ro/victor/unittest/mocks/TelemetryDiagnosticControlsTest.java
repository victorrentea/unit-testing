package ro.victor.unittest.mocks;

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
import static ro.victor.unittest.mocks.TelemetryClient.DIAGNOSTIC_MESSAGE;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
    @Mock
    private TelemetryClient mockClient;
    @InjectMocks
    private TelemetryDiagnosticControls controls;

    @Test
    public void checkTransmission_basicFlow() {
        when(mockClient.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission();
        verify(mockClient).disconnect();

        verify(mockClient).send(anyString()); // varianta pragmatica (aka siktir) - daca e un string variabil pe care iti e greu/n-ai chef sa il determini precis
        verify(mockClient).send("AT#UD"); // ce simti? PANICA. Cand e IO catre alt sistem, format extern
        verify(mockClient).send(DIAGNOSTIC_MESSAGE); // esti do[a]mn[a] -- as recomanda

        verify(mockClient).receive();
    }

    @Test(expected = IllegalStateException.class)
    public void bum() {
        when(mockClient.getOnlineStatus()).thenReturn(false);
        controls.checkTransmission();
    }

}

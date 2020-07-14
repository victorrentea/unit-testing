package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControls_CheckTransmissionTest {

    @Mock
    TelemetryClient clientMock;// = mock(TelemetryClient.class);

    @InjectMocks
    TelemetryDiagnosticControls controls;// = new TelemetryDiagnosticControls(clientMock);


    @Test
    public void disconnectsAndSends() {
        when(clientMock.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission();
        verify(clientMock).disconnect();

        verify(clientMock).send(anyString()); // orice string e bun -> error prome

        verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // a - mai mereu

        verify(clientMock).send("AT#UD"); // b -- cand stringul e parte dintr-un protocol de comm cu un sistem extern

    }

    @Test(expected = IllegalStateException.class)
    public void throwsWhenOffline() {
        when(clientMock.getOnlineStatus()).thenReturn(false);
        controls.checkTransmission();
    }

    @Test
    public void receives() {
        when(clientMock.getOnlineStatus()).thenReturn(true);
        when(clientMock.receive()).thenReturn("RECEIVE_VALUE");
        controls.checkTransmission();

//        verify(clientMock).receive();
        // daca ai programat o metoda cu when.thenReturn atunci nu prea are sens sa ii faci verify
        // ci codul testat ar trebui sa ia cumva deczii/ sa expuna datele citite din metoda mockuite

        assertEquals("RECEIVE_VALUE", controls.getDiagnosticInfo());
    }


}

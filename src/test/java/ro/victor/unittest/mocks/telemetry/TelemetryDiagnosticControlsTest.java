package ro.victor.unittest.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TelemetryDiagnosticControlsTest {
    private TelemetryClient mockClient;
    private TelemetryDiagnosticControls controls;

    // fix asta face Mockito.mocK:
//    new TelemetryClient() {
//        @Override
//        public String receive() {
//            return "ce vrea muchiu testului";
//        }
//    };


    @Before
    public void initialize() {
        mockClient = mock(TelemetryClient.class);
        controls = new TelemetryDiagnosticControls(mockClient);

    }
    @Test
    public void test() {
        // arrange
        when(mockClient.getOnlineStatus()).thenReturn(true);

        //act
        controls.checkTransmission();

        // assert
        verify(mockClient).disconnect();
        // ingeneral constante:
        verify(mockClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);

        verify(mockClient).send("AT#UD"); // cand ai vrea sa pui literal, nu constanta?
        // "cand AT#UD e cerut de un sistem extern" - Silviu
    }

    @Test(expected = IllegalStateException.class)
    public void throwWhenNoOnline() {
//        Mockito.withSettings().
        when(mockClient.getOnlineStatus()).thenReturn(false);

        controls.checkTransmission();
    }

}

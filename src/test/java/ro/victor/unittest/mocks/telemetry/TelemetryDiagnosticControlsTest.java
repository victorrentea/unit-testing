package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TelemetryDiagnosticControlsTest {

    // fix asta face Mockito.mocK:
//    new TelemetryClient() {
//        @Override
//        public String receive() {
//            return "ce vrea muchiu testului";
//        }
//    };
    @Test
    public void test() {
        // arrange
        TelemetryClient mockClient = mock(TelemetryClient.class);
        when(mockClient.getOnlineStatus()).thenReturn(true);
        TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls(mockClient);

        //act
        controls.checkTransmission();

        // assert
        verify(mockClient).disconnect();
        // ingeneral constante:
        verify(mockClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);

        verify(mockClient).send("AT#UD"); // cand ai vrea sa pui literal, nu constanta?
        // "cand AT#UD e cerut de un sistem extern" - Silviu
    }

}

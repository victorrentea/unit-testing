package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TelemetryDiagnosticControlsTest {

    @Test
    public void test() {
        // Intentia default ar trebui sa fie sa testtezi si clientul in joc cu Controls, eventual decupland sub client, cat mai aproape de exterior.
        TelemetryClient client = new TelemetryClient();
        TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls(client);
        controls.checkTransmission();

    }

}

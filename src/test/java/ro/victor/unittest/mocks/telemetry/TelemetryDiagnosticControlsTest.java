package ro.victor.unittest.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.none;
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
        controls.checkTransmission(0);
        verify(client).disconnect();
    }
    @Test(expected = IllegalStateException.class)
    public void throwsWhenNotOnline() {
        when(client.getOnlineStatus()).thenReturn(false);
        controls.checkTransmission(0);
    }

    @Test
    public void throwsWhenVIs1_oldSchool() {
        when(client.getOnlineStatus()).thenReturn(true);
        try {
            controls.checkTransmission(1);
            fail("Should have thrown exception");
        } catch (IllegalStateException e) {
            assertEquals("V is 1", e.getMessage());
        }
    }
    @Test
    public void throwsWhenVIs1_assertions() {
        when(client.getOnlineStatus()).thenReturn(true);
        assertThatThrownBy(() -> controls.checkTransmission(1))
            .hasMessageContaining("V is 1");
    }
    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void throwsWhenVIs1_junit() {
        expectedException.expectMessage("V is 1");
        when(client.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission(1);
    }


}

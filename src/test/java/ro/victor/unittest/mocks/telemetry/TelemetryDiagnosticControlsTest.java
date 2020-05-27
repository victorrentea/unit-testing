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
import static ro.victor.unittest.mocks.telemetry.TelemetryException.ErrorCode.VALUE_IS_1;


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
    @Test(expected = TelemetryException.class)
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
        } catch (TelemetryException e) {
            assertEquals("value is 1", e.getMessage());
        }
    }
    @Test
    public void throwsWhenVIs1_assertions() {
        when(client.getOnlineStatus()).thenReturn(true);
        assertThatThrownBy(() -> controls.checkTransmission(1))
            .hasMessageContaining("value is 1");
    }
    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void throwsWhenVIs1_junit() {
        expectedException.expectMessage("value is 1");
        when(client.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission(1);
    }

    @Test
    public void throwsWhenVIs1_junit_matchers() {
        expectedException.expect(new TelemetryExceptionMatcher(VALUE_IS_1));
        when(client.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission(1);
    }

    @Test
    public void checkTransmissionOk() {
        when(client.getOnlineStatus()).thenReturn(true);
        when(client.receive()).thenReturn("TATAIE");
        controls.checkTransmission(0);

        verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // foloseste constanta in general 90+%

//        verify(client).send("AT#UD"); -- cand vrei sa impiedici modificare avalorii
        // pt ca (tipic) apartine protocolului de comunicatie cu alt sistem
        // sau hardware -> aperi impotriva idio**lor care modifica valoare constantei

//        verify(client).receive();
        Assertions.assertThat(controls.getDiagnosticInfo()).isEqualTo("TATAIE");
    }


}

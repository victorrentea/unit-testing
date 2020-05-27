package ro.victor.unittest.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode.NORMAL;
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

        verify(client, times(1)).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // foloseste constanta in general 90+%

//        verifyNoMoreInteractions(client);
//        verify(client).send("AT#UD"); -- cand vrei sa impiedici modificare avalorii
        // pt ca (tipic) apartine protocolului de comunicatie cu alt sistem
        // sau hardware -> aperi impotriva idio**lor care modifica valoare constantei

//        verify(client).receive();
        Assertions.assertThat(controls.getDiagnosticInfo()).isEqualTo("TATAIE");
    }


    @Captor
    ArgumentCaptor<ClientConfiguration> configCaptor;
    @Test
    public void configuresClient() {
        when(client.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission(0);
//        ArgumentCaptor<ClientConfiguration> configCaptor = forClass(ClientConfiguration.class);
        verify(client).configure(configCaptor.capture());
        ClientConfiguration config = configCaptor.getValue();
        assertEquals(NORMAL, config.getAckMode());
    }

    @Test
    public void createConfigCorrectly() {
        when(client.getVersion()).thenReturn("VER");
        ClientConfiguration config = controls.createConfig();
        assertEquals(NORMAL, config.getAckMode());
        Assertions.assertThat(config.getSessionId()).startsWith("VER-");
        Assertions.assertThat(config.getSessionStart()).isNotNull(); // echipa siktir
        Assertions.assertThat(config.getSessionStart()).isEqualToIgnoringHours(LocalDateTime.now()); // echipa cu pretentii, si noroc in viata
    }

    @Test
    public void configuresClientWithWhatever() {
        when(client.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission(0);
        verify(client).configure(any());
        // nu e absolut tot testat inca:
        // poate sa se modif cod de prod astfeL :
        //telemetryClient.configure(new ClientConfig()); -- mutant ne prins de test

        // super-strict ar insemna sa citesti despre
        /** @see org.mockito.Spy */
        // tu ai vrea sa scrii:
//        verify(controls).createConfig();

    }

}

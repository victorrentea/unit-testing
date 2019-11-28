package ro.victor.unittest.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import static java.util.Date.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
    @Rule
    public ExpectedException expectedException = none();

    @Mock
    private TelemetryClient client;
//    @InjectMocks
    private TelemetryDiagnosticControls controls;
    private Clock clock = Clock.fixed(LocalDateTime.parse("2019-01-01T12:12:12").toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

    @Before
    public void setUp() throws Exception {
        controls = new TelemetryDiagnosticControls(client, clock); // ai nevoie doar daca testezi doua clase frati de sange inconjurati de fatzarnici (mockuri).
        when(client.getOnlineStatus()).thenReturn(true);
    }

    @Test
    public void throwsForOnlineStatusFalse() throws Exception {
        expectedException.expectMessage("connect");
        when(client.getOnlineStatus()).thenReturn(false);
        controls.checkTransmission();
    }
    @Test
    public void disconnectsClient() throws Exception {
        controls.checkTransmission();
        verify(client).disconnect();
    }

    @Test
    public void receivesDiagnosticInfo() throws Exception {
        when(client.receive()).thenReturn("Tataie");
        controls.checkTransmission();
        // verify(client,times(1)).receive(); // doar atunci are sens sa verifici un QUERY facut de obiectul testat.
        // ARE sens sa verifici daca: face side effects (nu e pura), costa bani, sau dureaza timp mult
        assertThat(controls.getDiagnosticInfo()).isEqualTo("Tataie");
    }

    @Test
    public void sendsDiagnosticMessage() throws Exception {
        controls.checkTransmission();
        verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
    }

    @Test
    public void configuresClient() throws Exception {
        ArgumentCaptor<ClientConfiguration> mincioc = ArgumentCaptor.forClass(ClientConfiguration.class);
        controls.checkTransmission();
        verify(client).configure(mincioc.capture());

        ClientConfiguration configDinProd = mincioc.getValue();
        assertThat(configDinProd.getAckMode()).isEqualTo(ClientConfiguration.AckMode.NORMAL);

        assertThat(configDinProd.getSessionStart()).isEqualTo(
                from(LocalDateTime.now(clock)
            .atZone(ZoneId.systemDefault())
            .toInstant()).getTime());

        assertThat(configDinProd.getSessionId()).isNotNull();
}


}

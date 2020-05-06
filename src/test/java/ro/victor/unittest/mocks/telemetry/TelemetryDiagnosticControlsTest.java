package ro.victor.unittest.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public class TelemetryDiagnosticControlsTest {
    public static final String USERNAME = "john";
    @Mock
    private TelemetryClient client;
    @InjectMocks
    private TelemetryDiagnosticControls controls;
//    @InjectMocks
//    private AltaClasa alta;
    @Captor
    private ArgumentCaptor<ClientConfiguration> configCaptor;

    @BeforeClass
    public void createMocks() {
        MockitoAnnotations.initMocks(this);
    }
    @BeforeMethod
    public void resetMocks() {
        Mockito.reset(client);
        // fixture: programez ce tre sa raspunda colaboratorii
        when(client.getOnlineStatus()).thenReturn(true);
    }

    @Test
    public void disconnects() {

        // call prod (ACT)
        controls.checkTransmission();

        // Verificare a rezultatelor
        verify(client).disconnect();
    }

    @Test
    public void sendsDiagnosticMessage() {
        // call prod (ACT)
        controls.checkTransmission();

        // Verificare a rezultatelor
        verify(client).send("AT#UD"); // pe API, ca sa fixezi datele trimise/primite de la un sistem extern
        verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        // sol3: in JSON pe disk de mock response sau 'expected request'
    }
// 1. in alt cod de prod se folosete "AT#UD"
// 2. cand alte sisteme (cod de pe alt git) foloseste literalul

    @Test
    public void basicWorkflow() {
        when(client.receive()).thenReturn("TATAIE");

        // call prod (ACT)
        controls.checkTransmission();

        // Verificare a rezultatelor
        verify(client).disconnect();
        verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
//        verify(client, times(1)).receive(); // inutila, mai putin cand GET-ul e scump
        assertThat(controls.getDiagnosticInfo()).isEqualTo("TATAIE");
    }

    @Test
    public void configuresClient() {
        controls.checkTransmission();
        verify(client).configure(configCaptor.capture());
        ClientConfiguration config = configCaptor.getValue();

        assertThat(config.getAckMode()).isEqualTo(ClientConfiguration.AckMode.NORMAL);
    }
}

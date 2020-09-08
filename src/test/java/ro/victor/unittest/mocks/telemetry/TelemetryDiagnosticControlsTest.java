package ro.victor.unittest.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
    @Mock
    private TelemetryClient client;
    @InjectMocks
    private TelemetryDiagnosticControls controls;

    @Before
    public void initialize() {
        when(client.getOnlineStatus()).thenReturn(true);
    }

    @Test
    public void disconnects() throws Exception {
        controls.checkTransmission();
        verify(client).disconnect();
    }

    @Test(expected = IllegalStateException.class)
//    @DisplayName("Expect ceva")
    public void throwsWhenNotOnline() throws Exception {
        when(client.getOnlineStatus()).thenReturn(false); // !!!!!!!
        controls.checkTransmission();
//        int actual = 1;
//        assertEquals(/*"Expect ceva", */1, actual);
    }
    @Test
    public void sendsDiagnosticInfo() throws Exception {
        controls.checkTransmission();
        verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }
    @Test
    public void receivesDiagnosticInfo() throws Exception {
//        Random r = new Random(1);
//        System.out.println(r.nextInt());
//        System.out.println(r.nextInt());
        when(client.receive()).thenReturn("received value");
        controls.checkTransmission();

        verify(client, times(1)).receive();  //
        // veriy pe query doar daca vrei sa te
        // asiguri ca nu face repetat o operatie SCUMPA (100ms)

        assertThat(controls.getDiagnosticInfo()).isEqualTo("received value");
    }
    @Captor
    private ArgumentCaptor<ClientConfiguration> configCaptor;
    @Test
    public void configuresWithAckNormal() {
        controls.checkTransmission();

        verify(client).configure(configCaptor.capture());

        ClientConfiguration config = configCaptor.getValue();
        assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
    }
}

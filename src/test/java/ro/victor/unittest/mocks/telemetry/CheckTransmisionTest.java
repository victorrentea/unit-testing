package ro.victor.unittest.mocks.telemetry;

import io.swagger.config.ConfigFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckTransmisionTest {
    @Mock
    private TelemetryClient mockClient;
    @Mock
    private ConfigurationFactory configFactoryMock;
    @InjectMocks
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
        // le face acum automa runnerul Mockito @RunWith
//        mockClient = mock(TelemetryClient.class);
//        controls = new TelemetryDiagnosticControls(mockClient);
        when(mockClient.getOnlineStatus()).thenReturn(true);

    }

    @Test
    public void basicFlow() {
        // arrange

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
    public void throwsWhenNoOnline() {
//        Mockito.withSettings().
        when(mockClient.getOnlineStatus()).thenReturn(false); // reprogramezi mockul altfel decat in @Before

        controls.checkTransmission();
    }

    @Test
    public void receives() {
        when(mockClient.getOnlineStatus()).thenReturn(true);
        when(mockClient.getOnlineStatus()).thenReturn(true);
        when(mockClient.getOnlineStatus()).thenReturn(true);
        when(mockClient.getOnlineStatus()).thenReturn(true);

        when(mockClient.receive()).thenReturn("TATAIE");
        controls.checkTransmission();


//        de fapt, times faci cand op respectiva in prod merge pe sisteme straine
        verify(mockClient, times(2)).getOnlineStatus();// daca apelul dureaza 1sec
        verify(mockClient, times(2)).getOnlineStatus();// daca apelul dureaza 1sec
//        verify(mockClient.getOnlineStatus(), times(1));// times() ai nevoie daca met poate intoarce valori diferite la apeluri succesive (non referntial trasparent)
        // verify(mockClient.save(anyObject()), times(2)); : dc rei sa verifici ca a facut 2 inserturi

//        verify(mockClient).receive(); // in general sa faci .verify pe o metoda pe care ai facut si thenReturn e inutil.
        assertThat(controls.getDiagnosticInfo()).isEqualTo("TATAIE");
        assertThat(Arrays.asList(2, 1, 3)).containsExactlyInAnyOrder(1, 2, 3);
//        assertEquals(1, Arrays.asList(2, 1, 3).size());
//        assertThat(Arrays.asList(2, 1, 3)).hasSize(1);
//        assertThat(Arrays.asList(2, 1, 3)).hasSize(1);
//        assertEquals("TATAIE", controls.getDiagnosticInfo());
    }

    @Captor
    private ArgumentCaptor<ClientConfiguration> configCaptor;

    @Test
    public void configuresClient() {
        ClientConfiguration config = new ClientConfiguration();
        when(mockClient.getVersion()).thenReturn("VersionNo");
        when(configFactoryMock.createConfiguration("VersionNo")).thenReturn(config);

        controls.checkTransmission();
        verify(mockClient).configure(config);
    }

}

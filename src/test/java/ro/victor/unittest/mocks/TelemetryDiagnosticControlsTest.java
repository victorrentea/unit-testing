package ro.victor.unittest.mocks;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.TelemetryClient;
import ro.victor.unittest.mocks.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.TelemetryDiagnosticControls;
import ro.victor.unittest.mocks.X;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ro.victor.unittest.mocks.TelemetryClient.DIAGNOSTIC_MESSAGE;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
    @Mock
    private TelemetryClient mockClient;
    @InjectMocks
    private TelemetryDiagnosticControls controls;

    @Test
    public void checkTransmission_basicFlow() {
        // Given
        when(mockClient.getOnlineStatus()).thenReturn(true);
        when(mockClient.receive()).thenReturn("EXPECTED_RECEIVE_VALUE");

        // When
        controls.checkTransmission();

        // Then
        verify(mockClient).disconnect();

        verify(mockClient).send(anyString()); // varianta pragmatica (aka siktir) - daca e un string variabil pe care iti e greu/n-ai chef sa il determini precis
        verify(mockClient).send("AT#UD"); // ce simti? PANICA. Cand e IO catre alt sistem, format extern
        verify(mockClient).send(DIAGNOSTIC_MESSAGE); // esti do[a]mn[a] -- as recomanda

//        verify(mockClient).receive(); // 99.9% din cazuri, sa faci verify() pe ceva ce faci si when.thenReturn() e inutil.
        // cu alte cuvinte nu e cazul sa verifici ca o functie tip "QUERY" (pure function) este chemata. De ob, valoarea din thenReturn
        // influenteaza cumva relevant executia pe care tu o observi

        verify(mockClient, times(1)).receive(); // 0.1% - cand trebuie sa faci verify ? - cand e o SCUMPA (200ms)
        assertEquals("EXPECTED_RECEIVE_VALUE", controls.getDiagnosticInfo());
    }

    @Captor
    private ArgumentCaptor<ClientConfiguration> configCaptor;

    @Test
    public void configuresClient() {
        when(mockClient.getOnlineStatus()).thenReturn(true);

        controls.checkTransmission();

        // mockul retine tot, ce functii i s-au chemat, dar si cu ce parametrii. Ii poti obtine ulterior
//        ArgumentCaptor<ClientConfiguration> configCaptor = ArgumentCaptor.forClass(ClientConfiguration.class);
        verify(mockClient).configure(configCaptor.capture());
        ClientConfiguration config = configCaptor.getValue();

        assertEquals(ClientConfiguration.AckMode.NORMAL, config.getAckMode());

        assertNotNull(config.getSessionStart()); // max(siktir)

//        assertEquals(LocalDateTime.now(), config.getSessionStart()); // ruleta ruseasca. +1ms

        LocalDateTime azi = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        assertEquals(azi, config.getSessionStart().truncatedTo(ChronoUnit.DAYS)); // ingineru din tine

        // mai geeky
        Assertions.assertThat(config.getSessionStart()).isEqualToIgnoringHours(LocalDateTime.now());
    }

    @Test
    public void createConfig() {
        ClientConfiguration config = controls.createConfiguration();
        assertEquals(ClientConfiguration.AckMode.NORMAL, config.getAckMode());
        Assertions.assertThat(config.getSessionStart()).isEqualToIgnoringHours(LocalDateTime.now());
    }


    @Test
    public void configuresClientCuOrice() {
        when(mockClient.getOnlineStatus()).thenReturn(true);
        controls.checkTransmission();
        verify(mockClient).configure(any());
        // daca vrei sa fii mai catolic decat papa, si sa verifici 100%
        // poate te duce gandul la
        /** @see org.mockito.Spy */
        // NU-L FOLOSI,
        // in loc, fa asa:

        // Extrage createConfiguration intr-o alta clasa numita ConfigFactory
        // testeaz-o p-aia, si apoi injecteaza un @Mock obisnuit in Controls.
    }

        @Test(expected = IllegalStateException.class)
    public void bum() {
        when(mockClient.getOnlineStatus()).thenReturn(false);
        controls.checkTransmission();
    }

}

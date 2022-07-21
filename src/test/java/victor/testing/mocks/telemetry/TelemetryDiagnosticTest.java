package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.MIN;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

//class Fake implements I {}
@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
    @Mock
    Client clientMock;
//    = new Client() {
//        @Override
//        public boolean getOnlineStatus() {
//            return true;
//        }
//    }; // = mock(TelemetryClient.class);
    @InjectMocks
    Diagnostic diagnostic;// = new TelemetryDiagnostic(mock);

    @Test
    void disconnects() {
        when(clientMock.getOnlineStatus()).thenReturn(true);

        // prod call
        diagnostic.checkTransmission(true);

        verify(clientMock).disconnect(true);
    }
    @Test
    void throwsWhenNotOnline() {
        when(clientMock.getOnlineStatus()).thenReturn(false);

        //in Spr Boot by default Mockito functioneaza prin generearea dinamica la runtime a unei subclase a clasei mele.
        System.out.println("Client mock este de fapt o clasa: "+ clientMock.getClass().getName());

        assertThatThrownBy(() -> diagnostic.checkTransmission(true))
                .isInstanceOf(IllegalStateException.class);

    }
    @Test
    void sends() {
        when(clientMock.getOnlineStatus()).thenReturn(true);

        // prod call
        diagnostic.checkTransmission(true);

        verify(clientMock).send(notNull());// null este any() dar nu este anyString()
//        verify(clientMock).send(any());// null este any() dar nu este anyString()
//        verify(clientMock).send(anyString()); // ai consumat 6 car degeaba

        // mai strict
        // refolosesti constanta din prod: PRO: are semantica (nume), si testul NU pica daca val ct se modifica
        verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE); // in general asta

        // pui literal: daca VREI sa pice testu cand val se schimba
        verify(clientMock).send("AT#UD"); // daca literalul este trimis/primit de la alte sistem (parte din API)
    }

    @Test
    void receivesDiagnosticInfo() {
        when(clientMock.getOnlineStatus()).thenReturn(true);
        when(clientMock.receive()).thenReturn("acelceva");

        diagnostic.checkTransmission(true);

        verify(clientMock).receive(); // deageaba (in afara cazului cand vreau sa vf ca 1 singura data a fost chemata).
        // cand ma doare de cate ori chem o functie:
        // 1) cand poate intoarce altceva a doua oara (eg SELECT, GET)
        // 2) cand functia chemata are side effects (eg INSERT, POST)
        // 3) daca functia dureaza timp.

        // cum se cheama o fucntie care intoarce aleasi rezultat si nu face side effects : FUNCTIE  PURE
        // o functie PURA nu ar trebui sa-ti pese de cate ori o chemi

        // daca AI NEVOIE Sa faci VERIFY pe aceeasi  fucntie pe care ai facut when..then >> fucntia fie face retea fie nu e PURA

        assertThat(diagnostic.getDiagnosticInfo()).isEqualTo("acelceva");
    }

    @Captor
    ArgumentCaptor<ClientConfiguration> configCaptor;

    @Test
    void configuresClient() {
        when(clientMock.getOnlineStatus()).thenReturn(true);

        diagnostic.checkTransmission(true);

        verify(clientMock).configure(configCaptor.capture());
        ClientConfiguration config = configCaptor.getValue();
        assertThat(config.getAckMode()).isEqualTo(NORMAL);

        // nu merge
//        assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now());
        assertThat(config.getSessionStart()).isNotNull(); // ingineru'
        assertThat(config.getSessionStart()).isCloseTo(now(),byLessThan(1, MINUTES)); // omu de stiinta'

    }

}

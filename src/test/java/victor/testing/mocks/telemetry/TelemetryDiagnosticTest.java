package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//class Fake implements I {}
@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
    @Mock
    Client clientMock; // = mock(TelemetryClient.class);
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


}

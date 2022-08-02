package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

//@MockitoSettings(strictness = Strictness.LENIENT) // NU nicioadata pe teste noi
@ExtendWith(MockitoExtension.class)
public class DiagnosticTest {
    @Mock
    private Client clientMock;
    @InjectMocks
    @Spy
    private Diagnostic target;

//@BeforeAll // 1 data inainte de toate testele
//public static void method() {
//}
    @BeforeEach
    final void before() {
        lenient(). // ok-ish
                when(clientMock.getOnlineStatus()).thenReturn(true);
    }

    @Test
    void throwsWhenOffline() {
        when(clientMock.getOnlineStatus()).thenReturn(false);

        assertThatThrownBy(() -> target.checkTransmission(true))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void exploreWithTrue() {
        // given adica contextul testului
        doReturn(new ClientConfiguration())
                .when(target).configureClient(any());
//        when(clientMock.getVersion()).thenReturn("ceva, nu-mi pasa, da tre sa fie");

        // when adica callu de prod
        target.checkTransmission(true);

        // then adica ce-a facut
        verify(clientMock).disconnect(true);
        verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE); // a in mod normal, unless:
        verify(clientMock).send("AT#UD"); // b sa sufere cel care modifica val constantei :
        // ca sa fie constient ce modifica .. cand  val asta e cunoscuta altor sisteme (DB, FE, Alt microl, Fisier)
    }

    @Test
    void receivesDiagnosticInfo() {
        when(clientMock.getVersion()).thenReturn("ceva, nu-mi pasa, da tre sa fie");
        when(clientMock.receive()).thenReturn("DE CE DOAMNE!?");

        target.checkTransmission(true);

        assertThat(target.getDiagnosticInfo()).isEqualTo("DE CE DOAMNE!?");
    }

    @Test // x 7
    void configuresClient() {
        ClientConfiguration config = target.configureClient("ver");

        assertThat(config.getAckMode()).isEqualTo(NORMAL);
        assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(10, SECONDS));// ingineru
        assertThat(config.getSessionId()).startsWith("VER-")
                .hasSize(40);
    }


    // AM UN CHANGE REQUEST !! sa fac uppercase
    // pe versiune cand generez sessionID


    @Captor
    ArgumentCaptor<ClientConfiguration> configCaptor;
}

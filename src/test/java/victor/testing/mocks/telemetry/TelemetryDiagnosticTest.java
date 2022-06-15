package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode.NORMAL;


@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
    @Mock
    TelemetryClient clientMock;
    @Mock
    ClientConfigurationFactory factoryMock;
    @InjectMocks
//    @Spy// SOLUTIA 2: @Spy (partial mocks) - NU O FACE. Sunt RELE.
    TelemetryDiagnostic target;

    @BeforeEach
    final void before() {
        when(clientMock.getVersion()).thenReturn("nu am nevoie de asta dar nah.. casa evit un NPE in fct pe care o chem");

    }
    @Test
    void throwsWhenNotOnline() {
        when(clientMock.getOnlineStatus()).thenReturn(false);
        assertThatThrownBy(() -> target.checkTransmission(true))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void happy() {
        when(clientMock.getOnlineStatus()).thenReturn(true);
        // solutia1: ingros setupul testului pe met publica ca sa suporte sa intre si prin met privata.
//        when(clientMock.getVersion()).thenReturn("nu am nevoie de asta dar nah.. casa evit un NPE in fct pe care o chem");
//        doReturn(new ClientConfiguration()).when(target).createConfig(); // in testul asta met createConfig nu va executa de fapt
        when(clientMock.receive()).thenReturn("aceeasivaloare"); // SOLUTIA 2: @Spy (partial mocks) - NU O FACE. Sunt RELE.

        target.checkTransmission(true);

        verify(clientMock).disconnect(true);
        verify(clientMock).configure(any()); // pragmatic merge
        verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        assertThat(target.getDiagnosticInfo()).isEqualTo("aceeasivaloare");
    }

}

// SOlutia 3: cea mai "creieroasa". Inseamna ca tie chiar iti pasa de codebase. (si ai un pic de curaj) < aici chemi pair programming.
class ClientConfigurationFactoryTest {

    @Test
    void createConfigTest() { // x 7 din astea
        // lipseste ceva aici
//        when(clientMock.getVersion()).thenReturn("ovaloareanoastra");
        ClientConfigurationFactory target = new ClientConfigurationFactory();

        ClientConfiguration config = target.createConfig("ovaloareanoastra");

        assertThat(config.getAckMode()).isEqualTo(NORMAL);
//      Assertions.assertTrue(config.getSessionId().startsWith("ovaloareanoastra-")); // failure messageul sucks!
        assertThat(config.getSessionId())
                .startsWith("OVALOAREANOASTRA-")
                .hasSizeGreaterThan(30);
        assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, MINUTES));

    }

}

package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode.NORMAL;


@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
    @Mock
    TelemetryClient clientMock;
    @InjectMocks
    TelemetryDiagnostic target;

    @Test
    void throwsWhenNotOnline() {
        assertThatThrownBy(() -> target.checkTransmission(true))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void happy() {
        when(clientMock.getOnlineStatus()).thenReturn(true);
        when(clientMock.receive()).thenReturn("aceeasivaloare");

        target.checkTransmission(true);

        verify(clientMock).disconnect(true);
        verify(clientMock).configure(any()); // pragmatic merge
        verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        assertThat(target.getDiagnosticInfo()).isEqualTo("aceeasivaloare");
    }


    @Test
    void createConfigTest() { // x 7 din astea
        // lipseste ceva aici
        when(clientMock.getVersion()).thenReturn("ovaloareanoastra");

        ClientConfiguration config = target.createConfig();

        assertThat(config.getAckMode()).isEqualTo(NORMAL);
//      Assertions.assertTrue(config.getSessionId().startsWith("ovaloareanoastra-")); // failure messageul sucks!
        assertThat(config.getSessionId())
                .startsWith("OVALOAREANOASTRA-")
                .hasSizeGreaterThan(30);
        assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, MINUTES));

    }

}

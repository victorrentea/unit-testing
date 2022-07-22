package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

//class Fake implements I {}
@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
    @Mock
    Client clientMock;
    @InjectMocks
    Diagnostic diagnostic;


    @BeforeEach
    final void before() {
        when(clientMock.getOnlineStatus()).thenReturn(true);
    }
    @Test
    void throwsWhenNotOnline() {
        when(clientMock.getOnlineStatus()).thenReturn(false);

        assertThatThrownBy(() -> diagnostic.checkTransmission(true))
                .isInstanceOf(IllegalStateException.class);
    }
    @Test
    void happy() {
        when(clientMock.receive()).thenReturn("acelceva");

        diagnostic.checkTransmission(true);

        verify(clientMock).disconnect(true);
        verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
        assertThat(diagnostic.getDiagnosticInfo()).isEqualTo("acelceva");
    }

    @Captor
    ArgumentCaptor<ClientConfiguration> configCaptor;

    @Test
    void configuresClient() {
        when(clientMock.getVersion()).thenReturn("ver");

        diagnostic.checkTransmission(true);

        verify(clientMock).configure(configCaptor.capture());
        ClientConfiguration config = configCaptor.getValue();
        assertThat(config.getAckMode()).isEqualTo(NORMAL);
        assertThat(config.getSessionStart()).isCloseTo(now(),byLessThan(1, MINUTES));
        assertThat(config.getSessionId())
                .startsWith("ver-")
                .hasSize(40)
                ;
    }

}

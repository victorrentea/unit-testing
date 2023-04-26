package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 5
class DiagnosticSpyTest {
  @Mock
  Client clientMock;
  @InjectMocks
  @Spy
  Diagnostic diagnostic;

  @BeforeEach
  final void before() {
    //lenient().when(clientMock.getVersion()).thenReturn("namnevoiedatrestepun");
  }
  @Test
  void disconnects() {
    when(clientMock.getOnlineStatus()).thenReturn(true);
    doNothing().when(diagnostic).configureClient(true);

    diagnostic.checkTransmission(true);

    verify(clientMock).disconnect(true);
  }

  @Test
  public void configuresClientDirect() {
    when(clientMock.getVersion()).thenReturn("ver");

    diagnostic.configureClient(true);

    verify(clientMock).configure(configCaptor.capture());
    ClientConfiguration config = configCaptor.getValue();
    assertThat(config.getSessionId())
            .startsWith("VER-")
            .hasSize("ver-".length() + 36);
    assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(10, SECONDS));
  } // x 10 teste


  // CR versiunea clientului concatenata la sessionId tre facuta upperCase

  @Captor
  ArgumentCaptor<ClientConfiguration> configCaptor;
}
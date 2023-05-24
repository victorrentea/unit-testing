package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiagnosticTest {
  @Mock
  Client client /*= mock(Client.class) by MockitoExtension*/;

  @InjectMocks
  Diagnostic diagnostic; // setTelemetryClient is called by MockitoExtension

  @BeforeEach
  final void before() {
    when(client.getOnlineStatus()).thenReturn(true);
  }
  @Test
  void clientDisconnects() {
    diagnostic.checkTransmission(true);
    verify(client).disconnect(true);
  }

  @Test
  void throwsIllegalStateWhenClientNotOnline() {
    when(client.getOnlineStatus()).thenReturn(false); // reprograms the return false not true

    assertThatThrownBy(() -> diagnostic.checkTransmission(false))
        .isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("Unable to connect.");
  }

  @Test
  void returnsDiagnosticMessage() {
    String info = "TEST";
    when(client.receive()).thenReturn(info);

    diagnostic.checkTransmission(true);

    assertThat(diagnostic.getDiagnosticInfo()).isEqualTo(info);
  }

  @Test
  void clientSendsDiagnosticMessage() {

    diagnostic.checkTransmission(true);

    verify(client).send(Client.DIAGNOSTIC_MESSAGE);
  }

  @Captor
  ArgumentCaptor<ClientConfiguration> configCaptor;/* = ArgumentCaptor.forClass(ClientConfiguration.class*///);

  @Test
  void clientConfigOK() {
    String clientVersion = "v";
    when(client.getVersion()).thenReturn(clientVersion);

    diagnostic.checkTransmission(true);

    // dear mock, do you remember what argument you got when called from prod
    // 2 lines above ?
    verify(client).configure(configCaptor.capture());
    ClientConfiguration config = configCaptor.getValue();
    assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
    assertThat(config.getSessionId()).startsWith("V-");
//    assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now());
    assertThat(config.getSessionStart()).isEqualToIgnoringSeconds(now()); // truncate time to minute
    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS)); // truncate time to minute
  }
}

package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class) // 4
@ExtendWith(MockitoExtension.class) // 5
class DiagnosticTest {
//class DiagnosticCheckTransmissionTest {
  @Mock
  Client clientMock; // = mock(...)
  @InjectMocks
  Diagnostic diagnostic; // inejcteaza orice @Mock de mai sus oricum poate (ctor, setter, private fields)
  // < oricum stie Spring sa faca DI, stie si Mockito

  @Test
  void disconnects() {
    // given (= contextul, environment in care chemi metoda testata)
    when(clientMock.getOnlineStatus()).thenReturn(true);

    // when
    diagnostic.checkTransmission(true);

    // then
    verify(clientMock).disconnect(true);
  }

  @Test
  void throwsWhenNotOnline() {
    // given
    when(clientMock.getOnlineStatus()).thenReturn(false);

    // when
    assertThatThrownBy(() -> diagnostic.checkTransmission(true))
            .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void sendsDiagnosticMessage() {
    // given
    when(clientMock.getOnlineStatus()).thenReturn(true);

    // when
    diagnostic.checkTransmission(true);

    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
//    verify(clientMock).send(anyString()); // nu-ti pasa/ e f dificil sa vf exact ce arg / deja ai verificat in alt test detaliile argumentului
  }

  @Test
  void receivesDiagnosticInfo() {
    when(clientMock.getOnlineStatus()).thenReturn(true);
    when(clientMock.receive()).thenReturn("ceva");

    diagnostic.checkTransmission(true);

    assertThat(diagnostic.getDiagnosticInfo()).isEqualTo("ceva");
    // supersedes the line below
//    verify(clientMock).receive();
  }

  @Test
  public void configuresClient() {
    when(clientMock.getOnlineStatus()).thenReturn(true);
    when(clientMock.getVersion()).thenReturn("ver");

    diagnostic.checkTransmission(true);

    verify(clientMock).configure(configCaptor.capture());
    ClientConfiguration config = configCaptor.getValue();
    assertThat(config.getSessionId())
            .startsWith("ver-")
            .hasSize("ver-".length() + 36);
    assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
//    assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now());// nu merge ca millis
//    assertThat(config.getSessionStart()).isNotNull();// prea'n siktir
    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(10, SECONDS));
  }
  @Captor
  ArgumentCaptor<ClientConfiguration> configCaptor;
}
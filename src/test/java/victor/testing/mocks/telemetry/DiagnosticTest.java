package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiagnosticTest {

  @Test
  void disconnects() {
    // given
    Client clientMock = Mockito.mock(Client.class);
    Diagnostic diagnostic = new Diagnostic();
    diagnostic.setTelemetryClient(clientMock);
    when(clientMock.getOnlineStatus()).thenReturn(true);

    // when
    diagnostic.checkTransmission(true);

    // then
    verify(clientMock).disconnect(true);
  }

  @Test
  void throwsWhenNotOnline() {
    // given
    Client clientMock = Mockito.mock(Client.class);
    Diagnostic diagnostic = new Diagnostic();
    diagnostic.setTelemetryClient(clientMock);
    when(clientMock.getOnlineStatus()).thenReturn(false);

    // when
    assertThatThrownBy(() -> diagnostic.checkTransmission(true))
            .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void sendsDiagnosticMessage() {
    // given
    Client clientMock = Mockito.mock(Client.class);
    Diagnostic diagnostic = new Diagnostic();
    diagnostic.setTelemetryClient(clientMock);
    when(clientMock.getOnlineStatus()).thenReturn(true);

    // when
    diagnostic.checkTransmission(true);

    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
//    verify(clientMock).send(anyString()); // nu-ti pasa/ e f dificil sa vf exact ce arg / deja ai verificat in alt test detaliile argumentului
  }
}
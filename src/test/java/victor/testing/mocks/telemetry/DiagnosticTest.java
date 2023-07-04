package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DiagnosticTest {

  @Test
  void disconnects() {
    // given
    Client clientMock = mock(Client.class);
    Diagnostic diagnostic = new Diagnostic(clientMock);
    when(clientMock.getOnlineStatus()).thenReturn(true);

    // when
    diagnostic.checkTransmission(true);

    // then
    verify(clientMock).disconnect(true);
  }
}
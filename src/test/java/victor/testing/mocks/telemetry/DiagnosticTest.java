package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiagnosticTest {

  Diagnostic diagnostic = new Diagnostic();
  Client clientMock = Mockito.mock(Client.class);
  @BeforeEach
  final void before() {
    diagnostic.setTelemetryClient(clientMock);
    when(clientMock.getVersion()).thenReturn("ver");
  }

  @Test
  void checkTransmission() {
    when(clientMock.getOnlineStatus()).thenReturn(true);

    diagnostic.checkTransmission(true);

    verify(clientMock).disconnect(true);
  }

  @Test
  void checkTransmission_throwsWhenNotOnline() {
    when(clientMock.getOnlineStatus()).thenReturn(false);

    assertThatThrownBy(() -> // AssertJ
        diagnostic.checkTransmission(true))
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Unable to connect.");

    //assert+verify
    verify(clientMock).disconnect(true);
  }
}
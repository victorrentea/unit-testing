package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiagnosticTest {
  @Mock
  Client clientMock;
  @InjectMocks
  Diagnostic diagnostic;

  @BeforeEach
  final void before() {
//    diagnostic.setTelemetryClient(clientMock); // inject mocks face asta aut0omat
  }

  @Test
  void checkTransmission() {
    when(clientMock.getOnlineStatus()).thenReturn(true);
    when(clientMock.getVersion()).thenReturn("ver");

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

    verify(clientMock).disconnect(true);
  }
}
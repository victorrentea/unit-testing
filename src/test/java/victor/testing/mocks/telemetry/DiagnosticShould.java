package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiagnosticShould {
  @Mock
  Client clientMock/* = mock(Client.class)*/;

  @Test
  public void disconnect() {
    Diagnostic diagnostic = new Diagnostic(clientMock);
    when(clientMock.getOnlineStatus()).thenReturn(true);
    final boolean FORCE = false; // "constanta locala"

    diagnostic.checkTransmission(FORCE);

    verify(clientMock).disconnect(FORCE);
  }

  @Test
  public void throwWhenNotOnline() {
    Diagnostic diagnostic = new Diagnostic(clientMock);
    when(clientMock.getOnlineStatus()).thenReturn(false);

    assertThatThrownBy(() -> diagnostic.checkTransmission(false))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Unable to connect.");
  }

  @Test
  public void sendsDiagnosticMessage() {
    Diagnostic diagnostic = new Diagnostic(clientMock);
    when(clientMock.getOnlineStatus()).thenReturn(true);
    final boolean FORCE = false; // "constanta locala"

    diagnostic.checkTransmission(FORCE);

    verify(clientMock).disconnect(FORCE);
  }

  @Test
  public void sendDiagnosticMessage() {
    Diagnostic diagnostic = new Diagnostic(clientMock);
    when(clientMock.getOnlineStatus()).thenReturn(true);
    when(clientMock.receive()).thenReturn("expectedValue");

    diagnostic.checkTransmission(false);

    // nu are sens sa faci verify decat pe COMMAND functions (ca sa te asiguri sa s-a efectuat side effectul
    //
    verify(clientMock).receive();
    // nu e nevoie sa verify() ce ai when..thenReturn
    // decat daca faci retea
    assertThat(diagnostic.getDiagnosticInfo())
        .isEqualTo("expectedValue");
  }
}

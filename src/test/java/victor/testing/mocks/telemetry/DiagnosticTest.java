package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Diagnostic.DIAGNOSTIC_CHANNEL_CONNECTION_STRING;

public class DiagnosticTest {

  @Test
  void disconnects() {
    Client mockClient = mock(Client.class);
    when(mockClient.getOnlineStatus()).thenReturn(true);
    Diagnostic sut = new Diagnostic(mockClient);

    sut.checkTransmission(true);

    verify(mockClient).disconnect(true);
  }
  @Test
  void connects() {
    Client mockClient = mock(Client.class);
    when(mockClient.getOnlineStatus()).thenReturn(false, true);
    Diagnostic sut = new Diagnostic(mockClient);

    sut.checkTransmission(true);

    verify(mockClient).connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
  }

  @Test
  void throwsWhenCannotConnect() {
    Client mockClient = mock(Client.class);
    when(mockClient.getOnlineStatus()).thenReturn(false);
    Diagnostic sut = new Diagnostic(mockClient);

    Assertions.assertThatThrownBy(()-> sut.checkTransmission(true))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Unable to connect.");
  }
}

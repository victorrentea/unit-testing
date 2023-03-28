package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Diagnostic.DIAGNOSTIC_CHANNEL_CONNECTION_STRING;

public class DiagnosticTest {

  private Client mockClient = mock(Client.class);
  // starting Mockito version ... there is a new magic lib in your classpath called mockito-inline
  // that superseded PowerMock and allows mocking of final classes and methods, static
//  private Client mockClient;
  private Diagnostic sut = new Diagnostic(mockClient);

  @Test
  void disconnects() {
    when(mockClient.getOnlineStatus()).thenReturn(true);

    sut.checkTransmission(true);

    verify(mockClient).disconnect(true);
  }
  @Test
  void connects() {
    when(mockClient.getOnlineStatus()).thenReturn(false, true);

    sut.checkTransmission(true);

    verify(mockClient).connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
  }

  @Test
  void throwsWhenCannotConnect() {
    when(mockClient.getOnlineStatus()).thenReturn(false);

    Assertions.assertThatThrownBy(()-> sut.checkTransmission(true))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Unable to connect.");
  }
}

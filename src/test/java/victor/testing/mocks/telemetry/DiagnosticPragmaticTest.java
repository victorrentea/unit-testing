package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Diagnostic.DIAGNOSTIC_CHANNEL_CONNECTION_STRING;

public class DiagnosticPragmaticTest {
  Client mockClient = mock(Client.class);
  Diagnostic sut = new Diagnostic(mockClient);

  // pragmatic 1 test for the entire happy flow : max 3 behavior pieces.
  // + less test code
  // - not specific tests, broad names
  // - harder to track failures,
  // - no more documentation purpose
  @Test
  void happy() {
    when(mockClient.getOnlineStatus()).thenReturn(true);
    when(mockClient.getVersion()).thenReturn("ver");
    when(mockClient.receive()).thenReturn("infoAFHAUIFYA&");

    sut.checkTransmission(true);

    verify(mockClient).disconnect(true);
    verify(mockClient).send(Client.DIAGNOSTIC_MESSAGE);
    verify(mockClient).receive();
    assertThat(sut.getDiagnosticInfo()).isEqualTo("infoAFHAUIFYA&");
  }
  @Test
  void connects() {
    when(mockClient.getVersion()).thenReturn("ver");
    when(mockClient.getOnlineStatus()).thenReturn(false, true);

    sut.checkTransmission(true);

    verify(mockClient).connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
  }

  @Test
  void throwsWhenCannotConnect() {
    when(mockClient.getOnlineStatus()).thenReturn(false);

    assertThatThrownBy(()-> sut.checkTransmission(true))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Unable to connect.");
  }




}
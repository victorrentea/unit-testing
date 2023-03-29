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
  @Test
  void disconnects() {
    when(mockClient.getOnlineStatus()).thenReturn(true);
    when(mockClient.getVersion()).thenReturn("ver");

    sut.checkTransmission(true);

    verify(mockClient).disconnect(true);
  }
  @Test
  void sendsGoodOutput() {
    when(mockClient.getOnlineStatus()).thenReturn(true);
    when(mockClient.getVersion()).thenReturn("ver");

    sut.checkTransmission(true);

    verify(mockClient).send(Client.DIAGNOSTIC_MESSAGE);
  }

  @Test
  void receivesDiagnosticInfo() {
    when(mockClient.getOnlineStatus()).thenReturn(true);
    when(mockClient.getVersion()).thenReturn("ver");
    when(mockClient.receive()).thenReturn("infoAFHAUIFYA&");

    sut.checkTransmission(true);

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
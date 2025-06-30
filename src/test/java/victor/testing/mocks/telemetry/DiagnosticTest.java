package victor.testing.mocks.telemetry;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

public class DiagnosticTest {

  public static final String DIAGNOSTIC_MESSAGE = "Diagnostic Info";

  Client client = mock(Client.class);
  Diagnostic diagnostic = new Diagnostic(client);

  @BeforeEach
  final void before() {
    when(client.getOnlineStatus()).thenReturn(true);
  }

  @Test
  void throwsWhenClientFailsToReconnect() {
    when(client.getOnlineStatus()).thenReturn(false); // override

    assertThatThrownBy(() -> diagnostic.checkTransmission(true))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Unable to connect.");
  }
  @Test
  void storesReceivedDiagnosticInfo() {
    when(client.receive()).thenReturn(DIAGNOSTIC_MESSAGE);

    diagnostic.checkTransmission(true);

    AssertionsForClassTypes.assertThat(diagnostic.getDiagnosticInfo()).isEqualTo(DIAGNOSTIC_MESSAGE);
  }
  @Test
  void sendsDiagnosticMessage() {
    diagnostic.checkTransmission(true);

    verify(client).send(Client.DIAGNOSTIC_MESSAGE);
  }
  @Test
  void disconnectsForce() {
    diagnostic.checkTransmission(true);

    verify(client).disconnect(true);
  }
  @Test
  void disconnects() {
    diagnostic.checkTransmission(false);

    verify(client).disconnect(false);
  }

//  @Captor
//  ArgumentCaptor<ClientConfiguration> configCaptor;
  @Test
  void configuresClientWithCorrectSessionId() {
    // STUB: program a method what to return
    when(client.getVersion()).thenReturn("1.0");
    var configCaptor = ArgumentCaptor.forClass(ClientConfiguration.class);

    var beforeTime = now();
    diagnostic.checkTransmission(true);

    verify(client).configure(configCaptor.capture());
    var config = configCaptor.getValue();
    assertThat(config.getAckMode()).isEqualTo(NORMAL);
    assertThat(config.getSessionStart()).isNotNull();// most common
//    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
    assertThat(config.getSessionStart()).isBetween(beforeTime, now());
//    assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now()); // ❌flaky
    assertThat(config.getSessionId())
        .startsWith("1.0-")
        .hasSize(40); // "1.0-" + 36-char UUID
  }
}

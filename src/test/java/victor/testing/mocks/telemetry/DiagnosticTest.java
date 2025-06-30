package victor.testing.mocks.telemetry;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT) // NEVER do this!
public class DiagnosticTest {
  public static final String DIAGNOSTIC_MESSAGE = "Diagnostic Info";

  @Mock
  Client client;
  @InjectMocks // via constructor, setter or private field
  Diagnostic diagnostic;

  @BeforeEach
  final void before() {
    lenient().when(client.getOnlineStatus()).thenReturn(true); // good
    // even better: split the test class fi big ± the prod class if complex enough
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

  @Test
  void createConfigTest2() {}
  void createConfigTest3() {}
  void createConfigTest4() {}
  @Test
  void createConfigUppercasesVersion() {
    when(client.getVersion()).thenReturn("v1");

    var config = diagnostic.createConfig();

    assertThat(config.getSessionId()).startsWith("V1");
  }


}

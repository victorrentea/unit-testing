package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

@ExtendWith(MockitoExtension.class)
public class DiagnosticShould {
  @Mock
  Client clientMock/* = mock(Client.class)*/;
//  Diagnostic diagnostic;
//  @BeforeEach
//  final void before() {
//    diagnostic = new Diagnostic(clientMock);
//  }
  @InjectMocks
  Diagnostic diagnostic;
  @Test
  public void disconnect() {
    when(clientMock.getOnlineStatus()).thenReturn(true);
    final boolean FORCE = false; // "constanta locala"

    diagnostic.checkTransmission(FORCE);

    verify(clientMock).disconnect(FORCE);
  }

  @Test
  public void throwWhenNotOnline() {
    when(clientMock.getOnlineStatus()).thenReturn(false);

    assertThatThrownBy(() -> diagnostic.checkTransmission(false))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Unable to connect.");
  }

  @Test
  public void sendsDiagnosticMessage() {
    when(clientMock.getOnlineStatus()).thenReturn(true);
    final boolean FORCE = false; // "constanta locala"

    diagnostic.checkTransmission(FORCE);

    verify(clientMock).disconnect(FORCE);
  }

  @Test
  public void sendDiagnosticMessage() {
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

  @Captor
  ArgumentCaptor<ClientConfiguration> captor;
  @Test
  public void configuresClient() {
    when(clientMock.getOnlineStatus()).thenReturn(true);

    diagnostic.checkTransmission(false);

    verify(clientMock).configure(captor.capture());
    ClientConfiguration config = captor.getValue();
    assertThat(config.getAckMode()).isEqualTo(NORMAL);
//    assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now()); // nu merge, sau mai rau merge doar pe masina mea

    // ingineru
    assertThat(config.getSessionStart()).isNotNull();
    // scientist
    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
    // arhitectu: sa facem wrap la apelul la now() intr-o componenta de spring mockuibila!
    //

    // hacker

  }
}

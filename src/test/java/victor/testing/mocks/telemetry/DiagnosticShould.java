package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.FLOOD;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

//@MockitoSettings(strictness = Strictness.LENIENT)
// Doamne fereste!@!!!!!! NU FA ASTA pentru ca altfel
// in @Before se va acumula gunoi: tot ce vreun test are nevoie poate fi pus acolo ->
// concluzie : in before e ca la ghena
// CAND ponceste un @Test in Before doar ce e necesar lui
// in before pui doar lucruri UTILE TUTUROR

@ExtendWith(MockitoExtension.class)
public class DiagnosticShould {
  public static final long CURRENT_TIME_MILLIS = 1L;
  @Mock
  UUIDGenerator uuidGenerator;
  @Mock
  Client clientMock/* = mock(Client.class)*/;
//  Diagnostic diagnostic;
//  @BeforeEach
//  final void before() {
//    diagnostic = new Diagnostic(clientMock);
//  }
  @InjectMocks
  Diagnostic diagnostic;

  @BeforeEach
  final void before() {
    lenient().when(clientMock.getVersion()).thenReturn("neversea");
  }
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
    when(clientMock.getVersion()).thenReturn("ver"); // overwrite primul stub
    when(uuidGenerator.uuid()).thenReturn("a");

    diagnostic.checkTransmission(false);

    verify(clientMock).configure(captor.capture());
    ClientConfiguration config = captor.getValue();
    assertThat(config.getAckMode()).isEqualTo(NORMAL);
    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
    assertThat(config.getSessionId()).isEqualTo("VER-a");
  }
  @Test
  public void ackModeIsFloodIfForced() {
    when(clientMock.getOnlineStatus()).thenReturn(true);

    diagnostic.checkTransmission(true);

    verify(clientMock).configure(argThat(arg -> arg.getAckMode() == FLOOD));
  }
}

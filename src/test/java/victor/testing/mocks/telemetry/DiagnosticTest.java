package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class) // 4
@ExtendWith(MockitoExtension.class) // 5

//@MockitoSettings(strictness = Strictness.LENIENT)
//= da-le-ncolo de when-then. nu-mi pasa. nu mai arunca UnnecessaryStubbingException!
  // dar renunti la protectia anti copy-spate
class DiagnosticTest {
//class DiagnosticCheckTransmissionTest {
  @Mock
  Client clientMock; // = mock(...)
  @InjectMocks
  Diagnostic diagnostic; // inejcteaza orice @Mock de mai sus oricum poate (ctor, setter, private fields)
  // < oricum stie Spring sa faca DI, stie si Mockito

  @BeforeEach
  final void before() {

    // daca mergi pe LENIENT mocking: ai voie sa nu fol un when-then
    // ==> se strange GUNOI in before: toti care au nevoie de vreun when-then pt 2+ teste -> il pun in before
    lenient().when(clientMock.getVersion()).thenReturn("namnevoiedatrestepun");
    // daca 1 singur test nu cheama metoda getVersion, e acceptabil sa pui lenient()

  }
  @Test
  void throwsWhenNotOnline() {
    // given
    when(clientMock.getOnlineStatus()).thenReturn(false);

    // when
    assertThatThrownBy(() -> diagnostic.checkTransmission(true))
            .isInstanceOf(IllegalStateException.class);
  }



  // squash multiple asserts into a single test
  // + less tests to maintain
  // - nu e specific testul: daca cade nu stii de ce direct.
  @Test
  void successfulTransmission() {
    when(clientMock.getOnlineStatus()).thenReturn(true);
    when(clientMock.receive()).thenReturn("ceva");

    diagnostic.checkTransmission(true);

    assertThat(diagnostic.getDiagnosticInfo()).isEqualTo("ceva");
    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
    verify(clientMock).disconnect(true);
  }

  @Test
  void disconnects() {
    // given (= contextul, environment in care chemi metoda testata)
    when(clientMock.getOnlineStatus()).thenReturn(true);

    // when
    diagnostic.checkTransmission(true);

    // then
    verify(clientMock).disconnect(true);
  }

  @Test
  void sendsDiagnosticMessage() {
    // given
    when(clientMock.getOnlineStatus()).thenReturn(true);

    // when
    diagnostic.checkTransmission(true);

    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
//    verify(clientMock).send(anyString()); // nu-ti pasa/ e f dificil sa vf exact ce arg / deja ai verificat in alt test detaliile argumentului
  }

  @Test
  void receivesDiagnosticInfo() {
    when(clientMock.getOnlineStatus()).thenReturn(true);
    when(clientMock.receive()).thenReturn("tataie");

    diagnostic.checkTransmission(true);

    assertThat(diagnostic.getDiagnosticInfo())
            .isEqualTo("tataie");
    // supersedes the line below
    verify(clientMock).receive(); // TOTUSI, daca vrei sa te asiguri ca faci o singura data
    // acel 'query' fetch de network -> poti face si verify() pe o metoda .thenReturn()
  }

//  @Test
//  public void configuresClient() {
//    when(clientMock.getOnlineStatus()).thenReturn(true);
//    when(clientMock.getVersion()).thenReturn("ver");
//
//    diagnostic.checkTransmission(true);
//
//    verify(clientMock).configure(configCaptor.capture());
//    ClientConfiguration config = configCaptor.getValue();
//    assertThat(config.getSessionId())
//            .startsWith("ver-")
//            .hasSize("ver-".length() + 36);
//    assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
////    assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now());// nu merge ca millis
////    assertThat(config.getSessionStart()).isNotNull();// prea'n siktir
//    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(10, SECONDS));
//  }

  @Test
  public void configuresWithAckModeTimeBoxedWhenForced() {
    when(clientMock.getOnlineStatus()).thenReturn(true);

    diagnostic.checkTransmission(false);

//    verify(clientMock).configure(configCaptor.capture());
//    ClientConfiguration config = configCaptor.getValue();
//    assertThat(config.getAckMode()).isEqualTo(AckMode.TIMEBOXED);
    verify(clientMock).configure(argThat(config -> config.getAckMode() == AckMode.TIMEBOXED));
  }


  @Test
  public void configuresClientDirect() {
    when(clientMock.getVersion()).thenReturn("ver");

    diagnostic.configureClient(true);

    verify(clientMock).configure(configCaptor.capture());
    ClientConfiguration config = configCaptor.getValue();
    assertThat(config.getSessionId())
            .startsWith("VER-")
            .hasSize("ver-".length() + 36);
    assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(10, SECONDS));
  } // x 10 teste


  // CR versiunea clientului concatenata la sessionId tre facuta upperCase

  @Captor
  ArgumentCaptor<ClientConfiguration> configCaptor;
}
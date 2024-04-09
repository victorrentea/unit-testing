package victor.testing.mocks.telemetry;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

@ExtendWith(MockitoExtension.class)
class DiagnosticTest {
  @Mock // = Mockito.mock(Client.class)*
  Client clientMock;

//  MapperCuMapStruct mapper = new MapperCuMapStructImpl();

  @InjectMocks // = new Diagnostic(); .setTelemetryClient(client_)
  Diagnostic sut;// = new Diagnostic(clientMock, mapper);

  @BeforeEach
  public void setup() {
    when(clientMock.getOnlineStatus()).thenReturn(true); // de obicei
  }

  @Test
  void checkTransmission() {
    when(clientMock.receive()).thenReturn("diagnostic info ceva");

    sut.checkTransmission(true);

    verify(clientMock).disconnect(true);
    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
    verify(clientMock).receive();// degeaba; uneori insa vrei sa verifici ca s-a chemat o singura data.
    assertThat(sut.getDiagnosticInfo()).isEqualTo("diagnostic info ceva");
  }

  @Test
  void throwsWhenNotOnline() {
    when(clientMock.getOnlineStatus()).thenReturn(false); // oaia neagra

    assertThatThrownBy(() -> sut.checkTransmission(true));
  }

  @Test
  void experiment() {
    sut.checkTransmission(true);

    var captor =
        forClass(ClientConfiguration.class);
    verify(clientMock).configure(captor.capture());
    ClientConfiguration config = captor.getValue();
    assertThat(config.getAckMode()).isEqualTo(NORMAL);
    assertThat(config.getSessionStart())
//        .isEqualTo(LocalDateTime.now());
        .isCloseTo(LocalDateTime.now(), byLessThan(1, ChronoUnit.SECONDS));
  }

  @Test
  void controllingTime() {

    LocalDateTime NOW = LocalDateTime.parse("2023-12-25T03:00:00");
    LocalDateTime PASTE = LocalDateTime.parse("2024-05-05T00:40:00");

    try (MockedStatic<LocalDateTime> staticMock = mockStatic(LocalDateTime.class)) {
      staticMock.when(LocalDateTime::now).thenReturn(NOW, PASTE);
      sut.checkTransmission(true);
    }
    var captor = forClass(ClientConfiguration.class);
    verify(clientMock).configure(captor.capture());
    ClientConfiguration config = captor.getValue();
    assertThat(config.getSessionStart()).isEqualTo(NOW);
  }

//  @AfterEach
//  public void method() {
//    Mockito.verifyNoMoreInteractions(client);
//  }
}

// A) param de LocalDateTime
// B) epsilon <-- 💖
// C) static mock 💖

//Mai puteti intalni
// @Autowired Clock clock;
// Provider<LocalDateTime> timeProvider;
package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

@ExtendWith(MockitoExtension.class)
class DiagnosticTest {

  @InjectMocks // = new Diagnostic(); .setTelemetryClient(client_)
  Diagnostic sut;
  @Mock // = Mockito.mock(Client.class)*
  Client clientMock;

  @Test
  void checkTransmission() {
    when(clientMock.getOnlineStatus())
        .thenReturn(true);
    when(clientMock.receive()).thenReturn("diagnostic info ceva");

    sut.checkTransmission(true, now());

    verify(clientMock).disconnect(true);
    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
    verify(clientMock).receive();// degeaba; uneori insa vrei sa verifici ca s-a chemat o singura data.
    assertThat(sut.getDiagnosticInfo()).isEqualTo("diagnostic info ceva");
  }

  @Test
  void throwsWhenNotOnline() {
    when(clientMock.getOnlineStatus())
        .thenReturn(false);

    assertThatThrownBy(() -> sut.checkTransmission(true, now()));
  }

  @Test
  void experiment() {
    when(clientMock.getOnlineStatus())
        .thenReturn(true);
    LocalDateTime NOW = now();

    sut.checkTransmission(true, NOW);

    var captor =
        ArgumentCaptor.forClass(ClientConfiguration.class);
    verify(clientMock).configure(captor.capture());
    ClientConfiguration config = captor.getValue();
    assertThat(config.getAckMode()).isEqualTo(NORMAL);
    assertThat(config.getSessionStart()).isEqualTo(NOW);
  }

//  @AfterEach
//  public void method() {
//    Mockito.verifyNoMoreInteractions(client);
//  }
}
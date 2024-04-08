package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    sut.checkTransmission(true);

    verify(clientMock).disconnect(true);
    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
//    verify(clientMock).receive();// degeaba
    assertThat(sut.getDiagnosticInfo()).isEqualTo("diagnostic info ceva");
  }

  @Test
  void throwsWhenNotOnline() {
    when(clientMock.getOnlineStatus())
        .thenReturn(false);

    assertThatThrownBy(() -> sut.checkTransmission(true));
  }

//  @AfterEach
//  public void method() {
//    Mockito.verifyNoMoreInteractions(client);
//  }
}
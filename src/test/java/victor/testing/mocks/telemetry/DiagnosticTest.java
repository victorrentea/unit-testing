package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
// dark ages before Mocks
//    Client client = new Client(){
//      @Override
//      public boolean getOnlineStatus() {
//        return true;
//      }
//    };

// if you add mockito-inline dependency killed PowerMock
@ExtendWith(MockitoExtension.class) // or MockitoAnnotations.openMocks(this)
public class DiagnosticTest {
  public static final String DIAGNOSTIC_INFO = "123";
  @Mock
  Client client;
  @InjectMocks
  Diagnostic diagnostic;

  @BeforeEach
  final void before() {
    when(client.getOnlineStatus()).thenReturn(true);
  }

  @Test
  void disconnects() {

    diagnostic.checkTransmission(true);

    verify(client).disconnect(true);
  }

  @Test
  void throwsWhenNotOnline() {
    // reprogram the mock overrides the prev stubbing
    when(client.getOnlineStatus()).thenReturn(false); // keep even though it's the default

    assertThatThrownBy(()->diagnostic.checkTransmission(true))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Unable to connect.");
  }

  @Test
  void sendsDiagnosticMessage() {

    diagnostic.checkTransmission(true);

//    verify(client).send(anyString()); // avoid whenever possible in verify()
    verify(client).send(Client.DIAGNOSTIC_MESSAGE);
  }

  @Test
  void receives() {
    when(client.receive(argThat(r->r.gCritical.equals("x")))).thenReturn(DIAGNOSTIC_INFO); // stubbing a method ("mocking")

    diagnostic.checkTransmission(true);

//    verify(client).receive(); // redundant
    // whenever you can check what happens without a mock, prefer it!
    assertThat(diagnostic.getDiagnosticInfo()).isEqualTo(DIAGNOSTIC_INFO);
  }

}

// doReturn
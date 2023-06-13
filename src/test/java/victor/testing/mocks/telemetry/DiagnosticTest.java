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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;
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
//    doReturn(false).when(client).getOnlineStatus();

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
//    when(client.receive(argThat(r->r.gCritical.equals("x")))).thenReturn(DIAGNOSTIC_INFO); // stubbing a method ("mocking")
//    doReturn(DIAGNOSTIC_INFO).when(client).receive(any()); // REQUIRED for @Spy/@SpyBean - code smells
    doReturn(1).when(client).receive(any()); // not type safe

    diagnostic.checkTransmission(true);

//    verify(client).receive(); // redundant
    // whenever you can check what happens without a mock, prefer it!
    assertThat(diagnostic.getDiagnosticInfo()).isEqualTo(DIAGNOSTIC_INFO);
  }

@Test
void configuresTheClient() {
  diagnostic.checkTransmission(true);

  // use this if you're looking for ONE FIELD
  verify(client).configure(argThat(config -> config.getAckMode() == NORMAL));

//  ArgumentCaptor<ClientConfiguration> configCaptor = ArgumentCaptor.forClass(ClientConfiguration.class);
  verify(client).configure(configCaptor.capture());
  ClientConfiguration config = configCaptor.getValue();
  assertThat(config.getAckMode()).isEqualTo(NORMAL);
}
@Captor
ArgumentCaptor<ClientConfiguration> configCaptor;




}

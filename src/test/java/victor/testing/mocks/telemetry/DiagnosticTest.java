package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;

//@RunWith(MockitoJUnitRunner.class) // JUnit4 equivalent
@ExtendWith(MockitoExtension.class) // JUnit5 extension care se ocupa cu initializarea instantei ede clasa de test
class DiagnosticTest {
  public static final String DIAG_INFO = "DIAG_INFO";
  @Mock
  Client clientMock;
  @InjectMocks // stie sa injecteze cum stie si Spring: contructor/field privat

  Diagnostic diagnostic;

  @Test
  void happy() {
    // given
    when(clientMock.getOnlineStatus()).thenReturn(true); // stubbing "eu stabuiesc o metoda"/ eu mockuiesc
    when(clientMock.receive()).thenReturn(DIAG_INFO);

    // when
    diagnostic.checkTransmission(true);

    // then
    verify(clientMock).disconnect(true); // verific
    verify(clientMock).send(DIAGNOSTIC_MESSAGE); // + mai compact cod
    verify(clientMock).receive(); //are times(1) automat. asta crapa testele acumBUM
    AssertionsForClassTypes.assertThat(diagnostic.getDiagnosticInfo()).isEqualTo(DIAG_INFO);
  }
  
  @Test
  void configuresClient() {
    when(clientMock.getOnlineStatus()).thenReturn(true);
    when(clientMock.getVersion()).thenReturn("ver");

    diagnostic.checkTransmission(true);

    verify(clientMock).configure(configCaptor.capture());
    ClientConfiguration config = configCaptor.getValue();
    assertThat(config.getAckMode()).isEqualTo(NORMAL);
//    assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now()); // pica de la millis
//    assertThat(config.getSessionStart()).isNotNull(); // ingineru'
    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(10, SECONDS)); // stiintifica
    assertThat(config.getSessionId())
        .startsWith("ver-")
        .hasSize(40);
  }

  // Traditional multe echipe injecteaza in singletoanele lor spring 2 dependinte stranii:
  //1) Clock (java.time)
  //2) UUIDGenerator (o clasa mica de-a lor pe care o pot mockui in liniste sa intoarca un UUID arbitrar)

  @Captor
  ArgumentCaptor<ClientConfiguration> configCaptor;

//  @Test
//  void sendsDiagnostic() { // +e mai narrow ce testezi "Single Assert Rule"
//    when(clientMock.getOnlineStatus()).thenReturn(true); // stubbing "eu stabuiesc o metoda"/ eu mockuiesc
//
//    diagnostic.checkTransmission(true);
//
//    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
//  }

  // in general eu testez tot pe unde trec ('happy flow') iar apoi testez branchurile separate cu @Test
  // si daca sunt anumite behaviouri critice (audit, notificiare), le testez seaprat

  @Test
  void throwsWhenNotOnline() {
    when(clientMock.getOnlineStatus()).thenReturn(false); // stubbing "eu stabuiesc o metoda"/ eu mockuiesc

    assertThatThrownBy(()->diagnostic.checkTransmission(true))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Unable to connect.");

  }
}
package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class) // JUnit4 equivalent
@ExtendWith(MockitoExtension.class) // JUnit5 extension care se ocupa cu initializarea instantei ede clasa de test
class DiagnosticTest {
  @Mock
  Client clientMock;
  @InjectMocks // stie sa injecteze cum stie si Spring: contructor/field privat

  Diagnostic diagnostic;

  @Test
  void disconnectsAndSends() {
    // given
    when(clientMock.getOnlineStatus()).thenReturn(true); // stubbing "eu stabuiesc o metoda"/ eu mockuiesc

    // when
    diagnostic.checkTransmission(true);

    // then
    verify(clientMock).disconnect(true);
    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE); // + mai compact cod
  }

//  @Test
//  void sendsDiagnostic() { // +e mai narrow ce testezi "Single Assert Rule"
//    when(clientMock.getOnlineStatus()).thenReturn(true); // stubbing "eu stabuiesc o metoda"/ eu mockuiesc
//
//    diagnostic.checkTransmission(true);
//
//    verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
//  }

  @Test
  void throwsWhenNotOnline() {
    when(clientMock.getOnlineStatus()).thenReturn(false); // stubbing "eu stabuiesc o metoda"/ eu mockuiesc

    assertThatThrownBy(()->diagnostic.checkTransmission(true))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Unable to connect.");

  }
}
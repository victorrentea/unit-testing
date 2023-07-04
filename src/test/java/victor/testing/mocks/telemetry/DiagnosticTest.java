package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class) // JUnit4 equivalent
@ExtendWith(MockitoExtension.class) // JUnit5 extension care se ocupa cu initializarea instantei ede clasa de test
class DiagnosticTest {
  @Mock
  Client clientMock;
  @InjectMocks // stie sa injecteze cum stie si Spring:
  // contructor/field privat

  Diagnostic diagnostic;

  @Test
  void disconnects() {
    // given
    when(clientMock.getOnlineStatus()).thenReturn(true);

    // when
    diagnostic.checkTransmission(true);

    // then
    verify(clientMock).disconnect(true);
  }
}
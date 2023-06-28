package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Purpose : mastering mocks

public class DiagnosticCheckTransmissionTest {
   @Test
   void disconnects() {
      // Mockito creates a subclass of the Client.class
      // (can't always do it 'final') unless you use
      // mockito-inline dependency in your pom, that deprecated PowerMock
      Client clientMock = mock(Client.class);
      Diagnostic diagnostic = new Diagnostic(clientMock);
      when(clientMock.getOnlineStatus()).thenReturn(true);

      diagnostic.checkTransmission(true);

      verify(clientMock).disconnect(true); // asking the mock if disconnect was really called from tested code
   }
   @Test
   void throwsWhenNotOnline() {
      Client clientMock = mock(Client.class);
      Diagnostic diagnostic = new Diagnostic(clientMock);
      when(clientMock.getOnlineStatus()).thenReturn(false);

      Assertions.assertThatThrownBy(() -> diagnostic.checkTransmission(true))
          .isInstanceOf(IllegalStateException.class);
   }
   @Test
   void sendsDiagnostic() {
      Client clientMock = mock(Client.class);
      Diagnostic diagnostic = new Diagnostic(clientMock);
      when(clientMock.getOnlineStatus()).thenReturn(true);

      diagnostic.checkTransmission(true);

      verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
   }
}

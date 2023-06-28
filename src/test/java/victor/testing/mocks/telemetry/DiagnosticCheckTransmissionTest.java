package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
   // Mockito creates a subclass of the Client.class
// (can't always do it 'final') unless you use
// mockito-inline dependency in your pom, that deprecated PowerMock

// Purpose : mastering mocks

// extension to the testing framework: it allows Mockito to create and setup the Test class instance (reflection on the fields to find @Mock/@InjectMocks)
//@RunWith(MockitoJUnitRunner.class) // Junit4 RULES
@ExtendWith(MockitoExtension.class) // Junit5
public class DiagnosticCheckTransmissionTest {
   @Mock
   Client clientMock;
   @InjectMocks
   Diagnostic diagnostic;

   @Test
   void disconnects() {
      when(clientMock.getOnlineStatus()).thenReturn(true);

      diagnostic.checkTransmission(true);

      verify(clientMock).disconnect(true); // asking the mock if disconnect was really called from tested code
   }
   @Test
   void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false);

      Assertions.assertThatThrownBy(() -> diagnostic.checkTransmission(true))
          .isInstanceOf(IllegalStateException.class);
   }
   @Test
   void sendsDiagnostic() {
      when(clientMock.getOnlineStatus()).thenReturn(true);

      diagnostic.checkTransmission(true);

      verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE);
   }
}

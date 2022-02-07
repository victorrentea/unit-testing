package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
   @Mock
   TelemetryClient clientMock;
   @InjectMocks
   TelemetryDiagnostic target;

   @Test
   void disconnects() {
      when(clientMock.getOnlineStatus()).thenReturn(true);

      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
   }
   @Test
   void sendsDiagnosticMessage() {
      when(clientMock.getOnlineStatus()).thenReturn(true);

      target.checkTransmission(true);

      // IF AND ONLY IF the constant value is part of a CONTRACT (exposed via some API)
      verify(clientMock).send("AT#UD");
      // otherwise (if it's an internal-only constant)
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }

   @Test
   void receivesDiagnosticInfo() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.receive()).thenReturn("SOMETHING######");

      target.checkTransmission(true);

//      verify(clientMock).receive(); // BAD: you shouldn't need to verify stubbed methods
      // NEEDED though only if the invoked method is:
      // a) NOT PURE (does side effects) eg. counter ++ <<< DESIGN MISTAKE
      // b) takes time
      // c) costs $$$
      // doing any of a,b,c multiple times = BAD => you need to verify()

      // NOTE! recent Mockito are STRICT: they check that every method stubbed (when..then) is really called from tested code
      assertThat(target.getDiagnosticInfo()).isEqualTo("SOMETHING######");
   }
// if you need to when..then AND verify the same method ===> you have a design issue. CQS violation

}



//class ProdCode { // FACADE pattern
//   private final SomeRepoA someRepoA;
//   private final SomeRepoB someRepoB;
//   public void method() {
//      A a = someRepoA.save();
//      B.a = a;
//      someRepoB.save(b);
//   }
//}
//
//
//interface SomeRepoA {
//   A save(A a);
//}
//
//interface SomeRepoB {
//   B save(B a);
//}
//
//class B {
//   A a;
//}
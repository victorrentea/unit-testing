package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class) // junit 4
public class TelemetryDiagnosticTest {

   @Mock
   TelemetryClient client; /* = Mockito.mock(TelemetryClient.class)*/
   @InjectMocks
   TelemetryDiagnostic target;

   @BeforeEach
   final void before() {
      when(client.getOnlineStatus()).thenReturn(true);
   }
   @Test
   void disconnects() {
      target.checkTransmission(true);

      verify(client).disconnect(true); // mocking = verify
   }

   @Test
   void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false); // mai expresiv ce ANUM e interesant

      assertThrows(IllegalStateException.class, () -> target.checkTransmission(true));
   }

   @Test
   void sendsDiagnostic() {
      target.checkTransmission(true);

      verify(client).send(any(), any());// cam lax
      verify(client).send(anyString(), any());// cam lax
      verify(client).send(eq(TelemetryClient.DIAGNOSTIC_MESSAGE), any()); // reuse de const din prod 80%-95%
      verify(client).send(eq("AT#UD"), any()); // repet constanta DOAR daca este cunoscuta si in afara codebaseului meu (ex DB, alte API, FIsiere, MQ)
   }

   @Test
   void receives() {
      when(client.receive()).thenReturn("::tataie::");

      target.checkTransmission(true);

//      verify(client).receive(); // useless in afara cazului cand  costa bani sau timp
      String actual = target.getDiagnosticInfo();
      Assertions.assertThat(actual).isEqualTo("::tataie::");
   }
}

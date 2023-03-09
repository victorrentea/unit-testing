package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class DiagnosticTest {


   private Diagnostic diagnostic;
   private Client client;

   @BeforeEach
   final void before() {
      diagnostic = new Diagnostic();
      client = mock(Client.class);
      diagnostic.setTelemetryClient(client);

   }
   @Test
   void throwsWhenNotOnline() {
      // an unstubbed mock method returns the 'null' value for the type,
      //  boolean=false, Boolean=null, List=emptyList
       assertThatThrownBy(() -> diagnostic.checkTransmission(true))
               .isInstanceOf(IllegalStateException.class);

   }

   @Test
   void disconnects() {
      when(client.getOnlineStatus()).thenReturn(true); // "mock a method" = "stubbing": i am teaching a method what return

       diagnostic.checkTransmission(true);

      verify(client).disconnect(true); // verify the call of a side-effecing function
   }


   @Test
   void sendsDiagnosticMessage() {
      when(client.getOnlineStatus()).thenReturn(true); // "mock a method" = "stubbing": i am teaching a method what return

      diagnostic.checkTransmission(true);

      verify(client).send(Client.DIAGNOSTIC_MESSAGE);
   }
}

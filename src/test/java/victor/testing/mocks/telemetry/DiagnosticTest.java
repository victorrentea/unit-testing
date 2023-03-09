package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // this extension to Junit reflection on the fields and sees @Mock @InjectMocks
public class DiagnosticTest {


   @InjectMocks
   private Diagnostic diagnostic;
   @Mock
   private Client client;

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

package victor.testing.mocks.telemetry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {

   @Mock
   private TelemetryClient client;

   @InjectMocks
   private TelemetryDiagnosticControls controls; // setter, ctor or private field injection

   @Test
   public void disconnects() {
      when(client.getOnlineStatus()).thenReturn(true);
      controls.checkTransmission(true);
      verify(client).disconnect(true);
   }

   @Test
   public void sendsDiagnosticMessage() {
      when(client.getOnlineStatus()).thenReturn(true);
      controls.checkTransmission(true);

//      verify(client).send(anyString()); // nu-mi pasa ce valoare imi da - riscanta

      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // refolosesc orbeste constanta
      // foloseste asta oricand constanta ta NU IESE din sistemul tau.

      verify(client).send("AT#UD"); // validez ca literalul este cel care trebuie
      // varianta asta o folosim doar daca : E UN PROTOCOL extern cu un sistem tertz (Seriala)
   }

   @Test
   public void receivesDiagnosticInfo() {
      when(client.getOnlineStatus()).thenReturn(true);
      when(client.receive()).thenReturn("tataie");

      controls.checkTransmission(true);

//      verify(client).receive(); // inutila
      assertEquals("tataie", controls.getDiagnosticInfo());
   }

   @Test(expected = IllegalStateException.class)
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      controls.checkTransmission(true);
   }

//   @Test
//   public void test() {
////   LdapUserApiClient client = new LdapUserApiClient();
//      LdapUserApiClient client = new LdapUserApiClient(){
//         @Override
//         public String getFullName(String username) {
////            super.findUser() NU cheama metoda originala !!
//            return "ldapCica";
//         }
//      };
//
//       Mockito.mock(LdapUserApiClient.class);
//      when(client.getFullName("a")).thenReturn("ldapCica");
//      UserService service = new UserService(client);
//
//      String result = service.biz();
//      assertEquals("LDAPCICA", result);
//   }
   
   
   @Test
   public void ifOfflineTriesToConnectOnce() {
      when(client.getOnlineStatus()).thenReturn(false, true);
      controls.checkTransmission(true);
      verify(client).connect(TelemetryDiagnosticControls.DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
   }
   @Test
   public void twoConnectAttempts() {
      when(client.getOnlineStatus()).thenReturn(false,false, true);
      controls.checkTransmission(true);
      verify(client, times(2)).connect(TelemetryDiagnosticControls.DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
   }




}
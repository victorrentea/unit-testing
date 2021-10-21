package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class) // junit 4
@MockitoSettings(strictness = Strictness.LENIENT) // in general fara
public class TelemetryDiagnosticTest {

   @Mock
   TelemetryClient client; /* = Mockito.mock(TelemetryClient.class)*/
   @InjectMocks
   TelemetryDiagnostic target;

   @BeforeEach
   final void before() {
      when(client.getOnlineStatus()).thenReturn(true);
      when(client.getVersion()).thenReturn("cevanenulcarenu-mitrebuiedefapt"); // degeaba
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
      assertThat(actual).isEqualTo("::tataie::");
   }

   @Test
   void configuresClient() {
      target.checkTransmission(true);

      verify(client).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      // imi inchipui ca am de scris multe teste care vor sa
      // asserteze campul AckMode din Client Configuration

      verify(client).configure(argThat(c -> c.getAckMode() == AckMode.NORMAL));
      verify(client).configure(configWithAckMode(AckMode.NORMAL));
   }

   @Test
   void createConfigDirectCall() {
      ClientConfiguration config = target.createConfigComplexa("ver");

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);

//      assertTrue(config.getSessionId().contains("VER-")); // junit 5 sucks here at failure message

      assertThat(config.getSessionId())
          .startsWith("VER-") //assert4j rocks!
          .hasSizeGreaterThan(20);
   }

   private ClientConfiguration configWithAckMode(AckMode normal) {
      return argThat(c -> c.getAckMode() == normal);
   }

   @Captor
   ArgumentCaptor<ClientConfiguration> configCaptor;

}

package victor.testing.mocks.telemetry;

import com.sun.org.apache.xalan.internal.xsltc.trax.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Disabled
public class TelemetryDiagnosticControlsTest extends TestBase{
   @Mock
   private TelemetryClient client;
   @Mock
   private ConfigurationFactory configurationFactory;
   @InjectMocks
   private TelemetryDiagnosticControls controls;

   @BeforeEach
   public void initialize2() {
      System.out.println("initul meu");
      when(client.getOnlineStatus()).thenReturn(true);
   }
   @Test
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      Assertions.assertThrows(IllegalStateException.class,
          () -> controls.checkTransmission());
   }

   @Test
   public void happy() {
      // given
      when(client.receive()).thenReturn("tataie");

      // when
      controls.checkTransmission();

      //then
      verify(client).disconnect();
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // 99% din cazuri
//      verify(client).send("AT#UD"); //1% <- daca e f important mesajul - iese in exterior app mele. MAI ALES DACA VB CU HARDWARE
      verify(client, times(1)).receive(); // doar daca e performance hit
      assertThat(controls.getDiagnosticInfo()).isEqualTo("tataie");
//      verifyNo
   }

   @Test
   public void configuresClient() {
      when(client.getVersion()).thenReturn("ver");
      controls.checkTransmission();
      verify(configurationFactory).createConfig("ver");
      verify(client).configure(notNull());
   }

}


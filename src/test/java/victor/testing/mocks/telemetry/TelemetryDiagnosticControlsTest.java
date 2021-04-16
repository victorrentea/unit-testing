package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
//public class TelemetryDiagnosticControlsWhenOfflineTest {
@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticControlsTest {
   @InjectMocks
   private TelemetryDiagnosticControls controls;
   @Mock
   private TelemetryClient telemetryClient;

   @BeforeEach
   public final void before() {
      when(telemetryClient.getOnlineStatus()).thenReturn(true);
   }

   @Test
   public void disconnects() {
      controls.checkTransmission(true);
      verify(telemetryClient).disconnect(true);
   }
   @Test
   public void throwsExceptionWhenOffline() {
      when(telemetryClient.getOnlineStatus()).thenReturn(false);
      assertThrows(IllegalStateException.class, () ->
          controls.checkTransmission(true)
      );
   }

   @Test
   public void sendsDiagnosticMessage() {
      controls.checkTransmission(true);

      // more readable
      verify(telemetryClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);

      // Guards against changes of the constant value : use for literals that are PART OF AN EXTERNAL CONTRACT
//      verify(telemetryClient).send("AT#UD");
//
//      verify(telemetryClient).send(anyString()); // lazyness
   }

   @Test
   public void receives() {
      when(telemetryClient.receive()).thenReturn("deda");
      controls.checkTransmission(true);

      assertEquals("deda", controls.getDiagnosticInfo());
//      verify(telemetryClient).receive(); // dont verify() methods that you stub (when())
   }
   
   @Test
   public void configuresWithACKModeNormal() {
       controls.checkTransmission(true);
      assertEquals(AckMode.NORMAL, controls.getConfig().getAckMode());
   }


}

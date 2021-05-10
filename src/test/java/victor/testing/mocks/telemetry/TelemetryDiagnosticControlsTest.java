package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticControlsTest {
   @Mock
   private TelemetryClient clientMock;
   @InjectMocks
   private TelemetryDiagnosticControls controls;

   @Test
   public void disconnects() {
      when(clientMock.getOnlineStatus()).thenReturn(true);

      controls.checkTransmission(false);

      verify(clientMock).disconnect(false);
   }

   @Test
   public void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false);

      assertThrows(IllegalStateException.class,
          () -> controls.checkTransmission(false));
   }

   @Test
   public void sendsDiagnosticMessage() {
      when(clientMock.getOnlineStatus()).thenReturn(true);

      controls.checkTransmission(false);

      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // 99% default
//      verify(clientMock).send("AT#UD"); //  if AT#UD is part of an extarnal Protocol (imagine SERIAL PORT)  smells like hardware over here<<<
   }

   @Test
   public void receives() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.receive()).thenReturn("strange");

      controls.checkTransmission(false);

//      verify(clientMock).receive(); useless: don't need to verify() stubbed methods (when.thenReturn)
      assertEquals("strange", controls.getDiagnosticInfo());
   }
}
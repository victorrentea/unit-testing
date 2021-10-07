package victor.testing.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TelemetryDiagnosticTest {

   private TelemetryDiagnostic diagnostic;
   private TelemetryClient clientMock;

   @Before
   public final void before() {
      diagnostic = new TelemetryDiagnostic();
      clientMock = mock(TelemetryClient.class);
      diagnostic.setTelemetryClient(clientMock);
   }
   @Test
   public void disconnects() {
      when(clientMock.getOnlineStatus()).thenReturn(true);

      diagnostic.checkTransmission(true);

      verify(clientMock).disconnect(true);
   }
   @Test(expected = IllegalStateException.class)
   public void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false);

      diagnostic.checkTransmission(true);
   }
}

/// X x = new X

/// --------asa merg mockurile: generand la runtime o subclasa a clasei "Victima" cu lib CGLIB
class X {
   public void method() {
      throw new IllegalArgumentException("rau");
   }
}
class XSublcasa extends X {
   @Override
   public void method() {
      // nimic aici :) totu bun!
   }
}
class Client {
   {
      X x1 = new X() {
         @Override
         public void method() {
            // bun
         }
      };
   }
}
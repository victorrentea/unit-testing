package victor.testing.mocks.telemetry;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class TelemetryDiagnosticTest {
   @Test
   public void disconnects() {
      TelemetryDiagnostic diagnostic = new TelemetryDiagnostic();
      TelemetryClient clientMock = mock(TelemetryClient.class);
      when(clientMock.getOnlineStatus()).thenReturn(true);
      diagnostic.setTelemetryClient(clientMock);

      diagnostic.checkTransmission(true);

      verify(clientMock).disconnect(true);
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
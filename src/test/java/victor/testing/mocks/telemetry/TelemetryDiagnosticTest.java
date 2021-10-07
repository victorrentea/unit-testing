package victor.testing.mocks.telemetry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class TelemetryDiagnosticTest {

   @MockBean
   private TelemetryClient clientMock;
   @Autowired
   private TelemetryDiagnostic diagnostic;

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
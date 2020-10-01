package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.*;

public class Test2 {
   @Mock
   private TelemetryClient clientMock;
   @Test
   public void disconnectsX() {
      Mockito.mockitoSession()
          .initMocks(this)
          .strictness(Strictness.STRICT_STUBS)
          .startMocking();

//      TelemetryClient clientMock = mock(TelemetryClient.class);
      when(clientMock.getOnlineStatus()).thenReturn(true);
      // nimic aici
   }
}

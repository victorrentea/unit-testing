package victor.testing.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticTest {

   @Mock
   private TelemetryClient clientMock;
   @Mock
   private ConfigFactory configFactoryMock;
   @InjectMocks
   private TelemetryDiagnostic diagnostic;

   @Before
   public final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
//      when(clientMock.getVersion()).thenReturn("de mama e nevoie sa pun asta aici");
      when(configFactoryMock.createConfig(any())).thenReturn(new ClientConfiguration());
   }


   @Test
   public void disconnects() {
      diagnostic.checkTransmission(true);

      verify(clientMock).disconnect(true);
   }

   @Test(expected = IllegalStateException.class)
   public void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false);

      diagnostic.checkTransmission(true);
   }

   @Test
   public void sendsDiagnosticMessage() {
      // when
      diagnostic.checkTransmission(true);

      // then
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }


//   @Mock
//   MyClock mockClock;
}

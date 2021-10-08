package victor.testing.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

import static org.mockito.Mockito.*;

class Impl1 extends TelemetryDiagnosticTest {
   @Override
   public ITelemetryDiagnostic testedObject() {
      return new TelemetryDiagnostic(clientMock, configFactoryMock);
   }
}
//class Impl2 extends TelemetryDiagnosticTest {
//   @Override
//   public ITelemetryDiagnostic testedObject() {
//      return new TelemetryDiagnostic2(clientMock, configFactoryMock);
//   }
//}

@RunWith(MockitoJUnitRunner.class)
public abstract class TelemetryDiagnosticTest {

   @Mock
   protected TelemetryClient clientMock;
   @Mock
   protected ConfigFactory configFactoryMock;
//   @InjectMocks
//   private ITelemetryDiagnostic diagnostic;

   public abstract ITelemetryDiagnostic testedObject();

   @Before
   public final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
//      when(clientMock.getVersion()).thenReturn("de mama e nevoie sa pun asta aici");
      when(configFactoryMock.createConfig(any())).thenReturn(new ClientConfiguration());
   }


   @Test
   public void disconnects() {
      testedObject().checkTransmission(true);

      verify(clientMock).disconnect(true);
   }

   @Test(expected = IllegalStateException.class)
   public void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false);

      testedObject().checkTransmission(true);
   }

   @Test
   public void sendsDiagnosticMessage() {
      // when
      testedObject().checkTransmission(true);

      // then
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }


//   @Mock
//   MyClock mockClock;
}

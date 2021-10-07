package victor.testing.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticTest {

   @Mock
   private TelemetryClient clientMock;
   @InjectMocks
   private TelemetryDiagnostic diagnostic;

   @Before
   public final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
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

   @Test
   public void configuresClientCorrectly() {
      // when
      diagnostic.checkTransmission(true);


      // then
      ArgumentCaptor<ClientConfiguration> configCaptor = forClass(ClientConfiguration.class);
      verify(clientMock).configure(configCaptor.capture());

      ClientConfiguration config = configCaptor.getValue();
      // cum mama masii iau instanta de config din metoda de prod :40??!
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
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
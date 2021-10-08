package victor.testing.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticTest {

   @Mock
   private TelemetryClient clientMock;
   @InjectMocks
   @Spy
   private TelemetryDiagnostic diagnostic;

   @Before
   public final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
//      when(clientMock.getVersion()).thenReturn("de mama e nevoie sa pun asta aici");
      doReturn(new ClientConfiguration()).when(diagnostic).createConfig(any());
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

//   @Test
//   public void configuresClientCorrectly() {
//      ClientConfiguration config = diagnostic.createConfig("ver");
//
//      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
//      assertThat(config.getSessionStart()).isCloseTo(now(), within(1, ChronoUnit.MINUTES));
//      assertThat(config.getSessionId()).startsWith("VER-");
//   }

//   @Mock
//   MyClock mockClock;
}

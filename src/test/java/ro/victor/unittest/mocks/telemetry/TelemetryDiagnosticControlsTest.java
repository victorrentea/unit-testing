package ro.victor.unittest.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class TelemetryDiagnosticControlsTest {
   @Mock
   private TelemetryClient clientMock;
   @InjectMocks
   private TelemetryDiagnosticControls controls;

   @Before
   public void initialize() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
   }

   @Test(expected = IllegalStateException.class)
   public void test() {
      when(clientMock.getOnlineStatus()).thenReturn(false);
      controls.checkTransmission();
   }

   @Test
   public void disconnects() {
      controls.checkTransmission();
      verify(clientMock).disconnect();
   }


   @Test
   public void sendsDiagnosticMessage() {
      controls.checkTransmission();
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
//      verify(client).send("AT#UD"); // Folositi DOAR atunci cand valoarea constantei VINE dintr-un sistem extern
   }

   @Test
   public void receives() {
      when(clientMock.receive()).thenReturn("tataie");
      controls.checkTransmission();
      assertEquals("tataie", controls.getDiagnosticInfo());
   }

   @Test
   public void configuresClient() {
      controls.checkTransmission();
      ArgumentCaptor<ClientConfiguration> captor = forClass(ClientConfiguration.class);
      verify(clientMock).configure(captor.capture());
      ClientConfiguration config = captor.getValue(); // corect, dar urat

//      ClientConfiguration config = controls.getConfig(); // greseala
      assertEquals(AckMode.NORMAL, config.getAckMode());
   }

}

package victor.testing.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.io.FileNotFoundException;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {

   @Mock
   private TelemetryClient clientMock;
   @Mock
   private ClientConfigurationFactory configurationFactoryMock;

   @InjectMocks
   private TelemetryDiagnosticControls controls; // setter, ctor or private field injection

   @Test
   public void happyFlow() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.receive()).thenReturn("tataie");

      controls.checkTransmission(true);

      verify(clientMock).disconnect(true);
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // refolosesc orbeste constanta
      assertEquals("tataie", controls.getDiagnosticInfo());
   }

   @Test(expected = IllegalStateException.class)
   public void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false); // reprogrameaza mockul ignorand ce era in @Befor einvatat sa faca
      controls.checkTransmission(true);
   }

   @Test
   public void ifOfflineTriesToConnectOnce() {
      when(clientMock.getOnlineStatus()).thenReturn(false, true);
      controls.checkTransmission(true);
      verify(clientMock).connect(TelemetryDiagnosticControls.DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
   }

   @Test
   public void twoConnectAttempts() {
      when(clientMock.getOnlineStatus()).thenReturn(false, false, true);
      controls.checkTransmission(true);
      verify(clientMock, times(2)).connect(TelemetryDiagnosticControls.DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
   }

   @Test
   public void passesTheCreatedConfigurationToTheClientConfigure() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.getVersion()).thenReturn("v");
      ClientConfiguration aConfig = new ClientConfiguration();
      when(configurationFactoryMock.configureClient("v"))
          .thenReturn(aConfig);

      controls.checkTransmission(true);

      verify(clientMock).configure(aConfig);
   }
   
}

@RunWith(MockitoJUnitRunner.class)
class ClientConfigurationFactoryTest {

   @Test
   public void configuresClient() throws FileNotFoundException {
      ClientConfiguration configuDatDinProd = new ClientConfigurationFactory().configureClient("ver#");

//      assertEquals(now(), configuDatDinProd.getSessionStart()); // nu merge ca sunt cateva mili intre
      assertThat(configuDatDinProd.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(configuDatDinProd.getSessionStart()).isNotNull(); // cel mai des
      assertThat(configuDatDinProd.getSessionStart()).isCloseTo(now(), within(1, MINUTES));

      //      assertTrue(  configuDatDinProd.getSessionId().startsWith("xVER#-")   );
      assertThat(configuDatDinProd.getSessionId())
          .startsWith("VER#-");
   }
}
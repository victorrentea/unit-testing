package ro.victor.unittest.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class TelemetryDiagnosticControlsTest {
   @Mock
   private TelemetryClient clientMock;
   @Mock
   private ClientConfigurationFactory configurationFactoryMock;
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
//      verify(clientMock).receive();// INUTILA in 99% din cazuri. pt ca ar trebui
      // sa verifici nu ca s-a chemat ci ca codul de prod pe care il testezi
      // a facut ceva ANUME cu ceea ce ai programat sa intoarca metoda
      assertEquals("tataie", controls.getDiagnosticInfo());
   }

   //   @Test
//   public void configuresClient() {
//      ClientConfiguration config = new ClientConfiguration();
//      when(configurationFactoryMock.createConfig(null)).thenReturn(config);
//
//      controls.checkTransmission();
//
//      verify(clientMock).configure(config);
//   }
   @Test
   public void configuresClient() {
      controls.checkTransmission();
      verify(clientMock).configure(notNull());
      verify(configurationFactoryMock).createConfig(anyString());
   }

}

package ro.victor.unittest.mocks.telemetry;

import org.hibernate.service.spi.InjectService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
   @Mock
   private TelemetryClient client;
   @Mock
   private UUIDGenerator uuidGenerator;
   @InjectMocks
   private ConfigFactory configFactory;
   private TelemetryDiagnosticControls controls;

   @Before
   public void initialize() {
      controls = new TelemetryDiagnosticControls(client, configFactory);
      when(client.getOnlineStatus()).thenReturn(true);
      when(client.getVersion()).thenReturn("UNUSED");
      when(uuidGenerator.generateRandom()).thenReturn("UUID");
   }

   @Test
   public void disconnects() throws Exception {
      controls.checkTransmission();
      verify(client).disconnect();
   }

   @Test(expected = IllegalStateException.class)
//    @DisplayName("Expect ceva")
   public void throwsWhenNotOnline() throws Exception {
      when(client.getOnlineStatus()).thenReturn(false); // !!!!!!!
      controls.checkTransmission();
//        int actual = 1;
//        assertEquals(/*"Expect ceva", */1, actual);
   }

   @Test
   public void sendsDiagnosticInfo() throws Exception {
      controls.checkTransmission();
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }

   @Test
   public void receivesDiagnosticInfo() throws Exception {
//        Random r = new Random(1);
//        System.out.println(r.nextInt());
//        System.out.println(r.nextInt());
      when(client.receive()).thenReturn("received value");
      controls.checkTransmission();

      verify(client, times(1)).receive();  //
      // veriy pe query doar daca vrei sa te
      // asiguri ca nu face repetat o operatie SCUMPA (100ms)

      assertThat(controls.getDiagnosticInfo()).isEqualTo("received value");
   }

   @Test
   public void configuresWithAckNormal() {
      when(client.getVersion()).thenReturn("VER");
      controls.checkTransmission();
      // TODO subred
   }

}

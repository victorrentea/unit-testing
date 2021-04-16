package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
//public class TelemetryDiagnosticControlsWhenOfflineTest {
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TelemetryDiagnosticControlsTest {
   @InjectMocks
   private TelemetryDiagnosticControls controls;
   @Mock
   private TelemetryClient telemetryClient;
   @Mock
   private ClientConfigurationFactory configurationFactory;

   @BeforeEach
   public final void before() {
      when(telemetryClient.getOnlineStatus()).thenReturn(true);
   }

   @Test
   public void disconnects() {
      controls.checkTransmission(true);
      verify(telemetryClient).disconnect(true);
   }

   @Test
   public void throwsExceptionWhenOffline() {
      when(telemetryClient.getOnlineStatus()).thenReturn(false);
      assertThrows(IllegalStateException.class, () ->
          controls.checkTransmission(true)
      );
//      Executors.newFixedThreadPool()

   }


   @Test
   public void sendsDiagnosticMessage() {
      controls.checkTransmission(true);

      // more readable
      verify(telemetryClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);

      // Guards against changes of the constant value : use for literals that are PART OF AN EXTERNAL CONTRACT
//      verify(telemetryClient).send("AT#UD");
//
//      verify(telemetryClient).send(anyString()); // lazyness
   }

   @Test
   public void receives() {
      when(telemetryClient.receive()).thenReturn("deda");
      controls.checkTransmission(true);

      assertEquals("deda", controls.getDiagnosticInfo());
//      verify(telemetryClient).receive(); // dont verify() methods that you stub (when())
   }

//   @Test
//   public void configuresWithACKModeNormal() {
//       controls.checkTransmission(true);
//      ArgumentCaptor<ClientConfiguration> configCaptor = ArgumentCaptor.forClass(ClientConfiguration.class);
//      verify(telemetryClient).configure(configCaptor.capture());
//      ClientConfiguration configPassedFromTestedCode = configCaptor.getValue();
//      assertEquals(AckMode.NORMAL, configPassedFromTestedCode.getAckMode());
//   }

   @Test
   public void configuresClient() {
      ClientConfiguration config = new ClientConfiguration();
      when(telemetryClient.getVersion()).thenReturn("vvv");
      when(configurationFactory.createConfig("vvv")).thenReturn(config);
      controls.checkTransmission(true);
      verify(telemetryClient).configure(config);
   }

   @Test
   public void configuresClientPragmatic() {
      controls.checkTransmission(true);
      verify(telemetryClient).configure(any());
   }

}


package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
//public class TelemetryDiagnosticControlsWhenOfflineTest {
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TelemetryDiagnosticControlsPragmaticTest {
   @Mock
   private TelemetryClient telemetryClient;
   @InjectMocks
   private ClientConfigurationFactory configurationFactory;
   private TelemetryDiagnosticControls controls;


   @BeforeEach
   public final void before() {
      when(telemetryClient.getOnlineStatus()).thenReturn(true);
      when(telemetryClient.getVersion()).thenReturn("vvv");

      controls = new TelemetryDiagnosticControls(telemetryClient, configurationFactory);
   }

   @Test
   public void throwsExceptionWhenOffline() {
      when(telemetryClient.getOnlineStatus()).thenReturn(false);
      assertThrows(IllegalStateException.class, () ->
          controls.checkTransmission(true)
      );
   }

   @Test
   public void happy() {
      when(telemetryClient.receive()).thenReturn("deda");

      controls.checkTransmission(true);

      verify(telemetryClient).disconnect(true);
      verify(telemetryClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      assertEquals("deda", controls.getDiagnosticInfo());
   }

}


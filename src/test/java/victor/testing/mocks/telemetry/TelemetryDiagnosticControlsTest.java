package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.fixture.AbstractTestBase;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticControlsTest extends AbstractTestBase {
   private TelemetryClient clientMock = mock(TelemetryClient.class);

   private ClientConfigurationFactory clientConfigurationFactory = new ClientConfigurationFactory();
   private TelemetryDiagnosticControls controls;

   @BeforeEach
   public final void before() {
      controls = new TelemetryDiagnosticControls();
      controls.setFactory(clientConfigurationFactory);
      controls.setTelemetryClient(clientMock);
   }
   @Test
   public void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false);

      assertThrows(IllegalStateException.class,
          () -> controls.checkTransmission(false));
   }

   @Test
   public void whenOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.receive()).thenReturn("strange");
      when(clientMock.getVersion()).thenReturn("ver");
      //mock the creatConfig method to NOT do anything . with a spy --> VERY BAD IDEA

      controls.checkTransmission(false);

//      verify(clientConfigurationFactory).createConfig("ver");
      verify(clientMock).disconnect(false);
      verify(clientMock).disconnect(false);
      verify(clientMock).configure(any());
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // 99% default
      assertEquals("strange", controls.getDiagnosticInfo());
   }

//   @Test
//   public void configuresClient() {
//      when(clientMock.getOnlineStatus()).thenReturn(true);
//      when(clientMock.getVersion()).thenReturn("ver");
//
//      controls.checkTransmission(false); // tsted code
//
//      verify(clientMock).configure(configCaptor.capture());
//
//      ClientConfiguration config = configCaptor.getValue();
//
//   }
//   @Captor
//   ArgumentCaptor<ClientConfiguration> configCaptor;


}

class ClientConfigurationFactoryTest {
   @Test
   public void createConfigOk() {
      ClientConfiguration config = new ClientConfigurationFactory().createConfig("ver");
      assertEquals(AckMode.NORMAL, config.getAckMode());
      assertNotNull(config.getSessionStart());
      Assertions.assertThat(config.getSessionId()).startsWith("VER-");
   }
}
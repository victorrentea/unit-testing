package ro.victor.unittest.mocks.telemetry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration;
import ro.victor.unittest.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientConfigTest {
   @Mock
   private UUIDGenerator uuidGenerator;
   @InjectMocks
   ConfigFactory factory;

   @Test
   public void createsConfig() {
      when(uuidGenerator.generateRandom()).thenReturn("U");
      ClientConfiguration config = factory.createConfig("VER");
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionId()).startsWith("VER-U");
   }
}

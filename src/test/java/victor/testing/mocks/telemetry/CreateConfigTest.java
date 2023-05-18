package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateConfigTest {
  @Mock Client client;
//  Client client = new Client() {
//    @Override
//    public String getVersion() {
//      return "ver";
//    }
//  };
  @Mock UUIDGenerator uuidGenerator;
  @InjectMocks Diagnostic diagnostic;
  @Test
  void explore() {
    when(client.getVersion()).thenReturn("ver");

    ClientConfiguration config = diagnostic.createConfig(true);

    assertThat(config.getSessionId()).startsWith("VER-");
    // + 7 teste din astea
  }
}

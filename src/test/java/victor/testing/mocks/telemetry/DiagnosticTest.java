package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DiagnosticTest {

  @Test
  void checkTransmission_disconnects() {
    Client client = Mockito.mock(Client.class);
    Diagnostic diagnostic = new Diagnostic();
    diagnostic.setTelemetryClient(client);
    Mockito.when(client.getOnlineStatus()).thenReturn(true);

    diagnostic.checkTransmission(true);

    Mockito.verify(client).disconnect(true);
  }
}


// @BeforeEach void init() {closeable = MockitoAnnotations.openMocks(this);
// @AfterEach void closeService() throws Exception {closeable.close();}

// doReturn
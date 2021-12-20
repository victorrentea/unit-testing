package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
   @Mock
   private TelemetryClient clientMock;
   @InjectMocks
   private TelemetryDiagnostic target;
   
   @Test
   void ok() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      // dynamic params
      when(clientMock.receive()).thenReturn("::message::");

      target.checkTransmission(true);

      assertThat(target.getDiagnosticInfo()).isEqualTo("::message::");
      verify(clientMock).disconnect(true);
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      verify(clientMock).receive(); // do not verify stubbing  -redundant

      // CQS prinicple

       // TODO ...
   }
}

package victor.testing.mocks.telemetry;

import org.assertj.core.api.AutoCloseableSoftAssertions;
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

//      try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//         // alternative for Junit4:  @Rule  public final VerificationCollector collector = MockitoJUnit.collector();
//         // https://stackoverflow.com/questions/53694359/how-to-use-soft-assertions-in-mockito
//         softly.assertThat(target.getDiagnosticInfo()).isEqualTo("::message::");
//         softly.assertThatThrownBy(() -> verify(clientMock).disconnect(true)).doesNotThrowAnyException();
//         softly.assertThatThrownBy(() -> verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE)).doesNotThrowAnyException();
//         softly.assertThatThrownBy(() -> verify(clientMock).receive()).doesNotThrowAnyException(); // do not verify stubbing  -redundant
//      }

      assertThat(target.getDiagnosticInfo()).isEqualTo("::message::");
      verify(clientMock).disconnect(true);
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      verify(clientMock).receive(); // do not verify stubbing  -redundant



      // CQS prinicple
 // why more than 3-4 asserions/ test are bad : SRP ?

       // TODO ...
   }
}

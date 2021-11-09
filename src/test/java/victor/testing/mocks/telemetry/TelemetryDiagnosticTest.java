package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
   @Mock
   TelemetryClient clientMock;
   @InjectMocks
   TelemetryDiagnostic target;

   @BeforeEach
   final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
   }
   @Test
   public void disconnects() {
      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
   }
   @Test
   public void sendsDiagnosticMessage() {
      target.checkTransmission(true);

      verify(clientMock).send(
          eq(TelemetryClient.DIAGNOSTIC_MESSAGE),
          any());
   }

   @Test
   public void receivesDiagnosticInfo() {
      when(clientMock.receive()).thenReturn("::tataie::"); // stubbing pe receive

      target.checkTransmission(true);

      assertThat(target.getDiagnosticInfo()).isEqualTo("::tataie::");
//      verify(clientMock).receive(); // 99% nu e nevoie sa-i faci si verify() daca i-ai facut when...then
      // care-i aia <1%? pe api externe
   }

   @Test
   public void throwsWhenNotOnline() {
      when(clientMock.getOnlineStatus()).thenReturn(false); // reprogramam mockul

      assertThrows(IllegalStateException.class,
          () -> target.checkTransmission(true));
   }
}
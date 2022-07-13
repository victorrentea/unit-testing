package victor.testing.mocks.telemetry;

import org.apache.kafka.server.authorizer.Authorizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
   @Mock
   private TelemetryClient client /*= new TelemetryClient() {
      @Override
      public boolean getOnlineStatus() {
         return true;
      }
   } */
   /*= mock(TelemetryClient.class)*/; // a mock is created and injected here
   @InjectMocks
   private TelemetryDiagnostic target; // the mock is injected in the dependecy of target

   @Test
   public void disconnects() {
      when(client.getOnlineStatus()).thenReturn(true); // stubbing
      // what the heck happens here in fact (the line above)

      target.checkTransmission(true);

      verify(client).disconnect(true); // mocking = verification that a method was invoked
   }

   @Test
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      Assertions.assertThrows(IllegalStateException.class, () ->
          target.checkTransmission(true));
   }

   @Test
   public void sendsDiagnosticInfo() {
      when(client.getOnlineStatus()).thenReturn(true);
      target.checkTransmission(true);
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }

   @Test
   public void receivesDiagnosticInfo() {
      // TODO inspect
      when(client.getOnlineStatus()).thenReturn(true);
      when(client.receive()).thenReturn("granpa"); // stubbing

      // act
      target.checkTransmission(true);

      assertThat(target.getDiagnosticInfo()).isEqualTo("granpa");
      verify(client).receive(); // should be needed,
      // Why would you ever want to verify() a method that you also when.,.then (stubbed)
      // to make sure the function that brought data to tested code is called only ONCE
      // WHY?
      // - expensive $ or time
      // - not referential transparent (diffrerent results 2nd time: relies on time, random, reads over NETWORK)
      // BUT if the method is NOT doing network => having  to verify() it means it's not CQS
   }

   @Test
   public void configuresClient() throws Exception {
      when(client.getOnlineStatus()).thenReturn(true);
      target.checkTransmission(true);
      verify(client).configure(any());
      // TODO check config.getAckMode is NORMAL
   }
}

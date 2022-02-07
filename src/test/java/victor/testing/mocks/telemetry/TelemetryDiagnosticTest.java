package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
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
   void disconnects() {

      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
   }
   @Test
   void sendsDiagnosticMessage() {

      target.checkTransmission(true);

      // IF AND ONLY IF the constant value is part of a CONTRACT (exposed via some API)
      verify(clientMock).send("AT#UD");
      // otherwise (if it's an internal-only constant)
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
   }

   @Test
   void receivesDiagnosticInfo() {
      when(clientMock.receive()).thenReturn("SOMETHING######");

      target.checkTransmission(true);

//      verify(clientMock).receive(); // BAD: you shouldn't need to verify stubbed methods
      // NEEDED though only if the invoked method is:
      // a) NOT PURE (does side effects) eg. counter ++ <<< DESIGN MISTAKE
      // b) takes time
      // c) costs $$$
      // doing any of a,b,c multiple times = BAD => you need to verify()

      // NOTE! recent Mockito are STRICT: they check that every method stubbed (when..then) is really called from tested code
      assertThat(target.getDiagnosticInfo()).isEqualTo("SOMETHING######");
   }
// if you need to when..then AND verify the same method ===> you have a design issue. CQS violation


   @Test
   void configuresTheClient() {
      target.checkTransmission(true);

      //capture the arg
//      ArgumentCaptor<ClientConfiguration> configCaptor = forClass(ClientConfiguration.class);
      verify(clientMock).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
   }
   @Captor
   ArgumentCaptor<ClientConfiguration> configCaptor;



   @Test
   void configIsOk() {
      when(clientMock.getVersion()).thenReturn("ver");

      ClientConfiguration config = target.createConfig();


      assertThat(config.getSessionId()).startsWith("ver-")
          .hasSize(40);
//      assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now()); // stupid > flaky  tests
      assertThat(config.getSessionStart()).isNotNull(); // engineer
      assertThat(config.getSessionStart()).isCloseTo(LocalDateTime.now(), byLessThan(1, MINUTES)); // science guy
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
   }

}



//class ProdCode { // FACADE pattern
//   private final SomeRepoA someRepoA;
//   private final SomeRepoB someRepoB;
//   public void method() {
//      A a = someRepoA.save();
//      B.a = a;
//      someRepoB.save(b);
//   }
//}
//
//
//interface SomeRepoA {
//   A save(A a);
//}
//
//interface SomeRepoB {
//   B save(B a);
//}
//
//class B {
//   A a;
//}
package victor.testing.mocks.telemetry;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT) // BAD IDEA
@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
   @Mock
   TelemetryClient clientMock;
   @Mock
   ConfigFactory configFactoryMock;

   @InjectMocks
   TelemetryDiagnostic target;

   @BeforeEach
   final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
   }

   //separate tests: helps document the code
//   @Test
//   void disconnects() {
//      target.checkTransmission(true);
//
//      verify(clientMock).disconnect(true);
//   }
//   @Test
//   void sendsDiagnosticMessage() {
//      target.checkTransmission(true);
//
//      verify(clientMock).send("AT#UD");
//   }

   // 'happy path test' : CONS: name of the test will not help you anymore.
   @Test
   void ok() {
      when(clientMock.receive()).thenReturn("SOMETHING######");

      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
      verify(clientMock).send("AT#UD");
      assertThat(target.getDiagnosticInfo()).isEqualTo("SOMETHING######");
   }




   @Test
   void configuresTheClient() {
      target.checkTransmission(true);
      verify(clientMock).configure(configCaptor.capture());

      verify(clientMock).configure(argThat(cc -> check(cc)));

      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
   }
   @Captor
   ArgumentCaptor<ClientConfiguration> configCaptor;

   public boolean check(ClientConfiguration config) {
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(LocalDateTime.now(), byLessThan(1, MINUTES));
      return true;
   }

//   @Test
//   void configuresClient() {
//      ClientConfiguration someConfig = new ClientConfiguration();
////      when(target.createConfig()).thenReturn(someConfig);
//      doReturn(someConfig).when(target).createConfig();
//
//      target.checkTransmission(true);
//
////      verify(clientMock).configure(notNull());
//      verify(clientMock).configure(someConfig);
//   }
}

class ConfigFactoryTest {
   private final ConfigFactory configFactory = new ConfigFactory();
   @Test
   void configIsOk() { // x10 tests -- more comfortable to write more tests.
      ClientConfiguration config = configFactory.createConfig("ver");

      try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
         softly.assertThat(config.getSessionId()).startsWith("VER-")
             .hasSize(40);
//      softly.assertThat(config.getSessionStart()).isEqualTo(LocalDateTime.now()); // stupid > flaky  tests
         softly.assertThat(config.getSessionStart()).isNotNull(); // engineer
         softly.assertThat(config.getSessionStart()).isCloseTo(LocalDateTime.now(), byLessThan(1, MINUTES)); // science guy
         softly.assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      } // to fix the tech issue of seeing the first failure only
      // supporting the idea of "Single Assert Rule"
   }

}

// when a fixture gets bigger > refactor>
// a) break the class



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
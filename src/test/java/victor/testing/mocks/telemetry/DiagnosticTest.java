package victor.testing.mocks.telemetry;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;


//   @Test
//   void happyFlow() {
//      when(clientMock.getOnlineStatus()).thenReturn(true);
//      when(clientMock.receive()).thenReturn("tataie");
//
//      target.checkTransmission(true);
//
//      verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE); // 99%
//      verify(clientMock).receive(); // NOT NEEDED0
//      assertThat(target.getDiagnosticInfo()).isEqualTo("tataie");
//      verify(clientMock).configure(configCaptor.capture());
//      ClientConfiguration config = configCaptor.getValue();
//      assertThat(config.getAckMode()).isEqualTo(NORMAL);
//      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
//      // not common unless targetting a critical API
////      verify(clientMock,never()).reportBadPayerToGovCreditOffice("qa");
////      verifyNoInteractions(clientMock); //
//   }
@ExtendWith(MockitoExtension.class)

// #1 DONT' USE : global per class .
//@MockitoSettings(strictness = Strictness.LENIENT) // only for migrating from Mockito 1. to 2.
public class DiagnosticTest {
   @Mock
   private Client clientMock;
   @Mock
   private ClientConfigurationFactory clientConfigurationFactory;
   @InjectMocks
   private Diagnostic target;

   @BeforeEach
   final void before() {
      // #2 just some stubbing
      lenient().when(clientMock.getOnlineStatus()).thenReturn(true);
   }

   @Test
   void throwsWhenOffline() {
      //re-stub the mock to do a differnt thing (black sheep). 1-2 times/class -- ok
      when(clientMock.getOnlineStatus()).thenReturn(false);

      Assert.assertThrows(IllegalStateException.class,
              () -> target.checkTransmission(true));
   }
   @Test
   void disconnects() {
      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
   }

   @Test
   void sendsDiagnostic() {

      target.checkTransmission(true);

      verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE); // 99%
   }

   @Test
   void receives() {
      when(clientMock.receive()).thenReturn("tataie");

      target.checkTransmission(true);

      verify(clientMock).receive(); // NOT NEEDED0
      assertThat(target.getDiagnosticInfo()).isEqualTo("tataie");
   }


//   @Test
//   void configuresClient() {
//      reset(target);
//
//      target.checkTransmission(true);
//
//      verify(clientMock).configure(configCaptor.capture());
//      ClientConfiguration config = configCaptor.getValue();
//      assertThat(config.getAckMode()).isEqualTo(NORMAL);
//      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
//   }
}

class ClientCOnfigurationFactoryTest {
   @Test
   void configuresClientDirectCall() { // x 10 tests
      ClientConfiguration config = new ClientConfigurationFactory().createConfig("ver");

      assertThat(config.getSessionId()).startsWith("VER-");
      assertThat(config.getAckMode()).isEqualTo(NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}

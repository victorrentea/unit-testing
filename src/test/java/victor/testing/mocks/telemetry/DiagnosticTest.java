package victor.testing.mocks.telemetry;

import com.sun.xml.bind.v2.TODO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;
import victor.testing.tools.WireMockExtension;

import javax.annotation.RegEx;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//@AutoConfigureWireMock // spring manages to reuse the same WM between test classes
@ExtendWith(MockitoExtension.class) // this extension to Junit reflection on the fields and sees @Mock @InjectMocks
//@MockitoSettings(strictness = Strictness.LENIENT) // < NEVER
public class DiagnosticTest {
   @InjectMocks
   @Spy
   private Diagnostic diagnostic;
   @Mock
   private Client client;

   @BeforeEach
   final void before() {
      // not clear for you tomorrow what part of this before is used
      // by the failing test you are trying to fix

//      lenient().when(client.getVersion()).thenReturn("ver"); // tolerable for 1 stubbing that
         // 70% of tests are using bellow: for zero impact calls eg for some config:
//      lenient().when(featureFlagsService.getFlag(FFlag.STUFF)).thenReturn(true);


//      when(client.getOnlineStatus()).thenReturn(true);
//      when(client.receive()).thenReturn("Gyros");
   }

   @Test
   void throwsWhenNotOnline() {
      // an unstubbed mock method returns the 'null' value for the type,
      //  boolean=false, Boolean=null, List=emptyList
      assertThatThrownBy(() -> diagnostic.checkTransmission(true))
              .isInstanceOf(IllegalStateException.class)
              .hasMessageContaining("connect") // checking exception
      // PRO: same exception with different reasons
      // CONS: brittle fragile test becasue I test presentation
      // "fix": use ENUMs to distinguish betw error codes
      ;

   }

   @Test
   void disconnects() {
//      doNothing().when(diagnostic).createConfig(); // vor a VOID-returnign method
      doReturn(new ClientConfiguration()).when(diagnostic).createConfig(); // for method returning stuff

      when(client.getOnlineStatus()).thenReturn(true); // "mock a method" = "stubbing": i am teaching a method what return

      diagnostic.checkTransmission(true);

      verify(client).disconnect(true); // verify the call of a side-effecing function
   }


   @Test
   void sendsDiagnosticMessage() {
      doReturn(new ClientConfiguration()).when(diagnostic).createConfig(); // for method returning stuff


      when(client.getOnlineStatus()).thenReturn(true); // "mock a method" = "stubbing": i am teaching a method what return

      diagnostic.checkTransmission(true);

      verify(client).send(Client.DIAGNOSTIC_MESSAGE); // <- default 99%

      verify(client).send("AT#UD"); // <- only if you want to freeze that value, ie. fail the constant value changes
   }

   @Test
   void receives() {
      doReturn(new ClientConfiguration()).when(diagnostic).createConfig(); // for method returning stuff


      // given <- use comments for test >7-10 lines long
      when(client.getOnlineStatus()).thenReturn(true); // "mock a method" = "stubbing": i am teaching a method what return
      when(client.receive()).thenReturn("Gyros");

      // when
      diagnostic.checkTransmission(true);

//      verify(client).receive(); // insufficient, actually USELESS
      assertThat(diagnostic.getDiagnosticInfo())
              .isEqualTo("Gyros");
   }

//   @Test
//   void achModeNor mal() {
//      when(client.getOnlineStatus()).thenReturn(true); // "mock a method" = "stubbing": i am teaching a method what return
//
//      diagnostic.checkTransmission(true);
//
//      // extract the value of the parameter from the mock
//      verify(client).configure(configCaptor.capture());
//      ClientConfiguration config = configCaptor.getValue();
//
//      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
////      assertThat(config.getSessionStart()).isEqualTo(now()); // fails due to millisconds passing from prod up to here
//      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS)); // scientist
//      assertThat(config.getSessionStart()).isNotNull(); // engineer will do
//
//      System.out.println(config.getSessionId());
//      assertThat(config.getSessionId())
//              .startsWith("ver-")
//              .hasSize(40); // approval testing: we copy the actual in the test after manually (visually) checking the result
//      //  "freeze-test"
//
//      // or if you want to test a single field
//      verify(client).configure(argThat(c-> c.getAckMode() == AckMode.NORMAL));
//   }

   @Captor
   ArgumentCaptor<ClientConfiguration> configCaptor;


// CR: the version for the client upper case in the session ID
   @Test
   void createConfigDirectly() {
      when(client.getVersion()).thenReturn("ver");

      ClientConfiguration config = diagnostic.createConfig();

      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS)); // scientist
      assertThat(config.getSessionId())
              .startsWith("VER-")
              .hasSize(40);
   } // x 11 more such tests

}

// TODO:
// test naming
// single assert rule
// abusing before / resetting mocks
// captor
// asserting exceptions

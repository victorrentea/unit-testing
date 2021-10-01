package victor.testing.mocks.telemetry;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TelemetryDiagnosticControlsTest {
//   @Mock
   private TelemetryClient client=mock(TelemetryClient.class);
//   @InjectMocks
   private TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls(client, new FriendClasssDeeplyConnectedToMe());


   @BeforeEach
   public final void before() {
      when(client.getOnlineStatus()).thenReturn(true);
      when(client.receive()).thenReturn("useless");

   }

   @Test
   public void throwsWhenNotOnline() {
//      when(client.receive()).thenReturn("useless");
      when(client.getOnlineStatus()).thenReturn(true, false);
      assertThrows(IllegalStateException.class, () ->
          controls.checkTransmission(true));
   }

   @Test
   public void receiveThrows() {
      when(client.getOnlineStatus()).thenThrow(new RuntimeException());
      assertThrows(RuntimeException.class, () ->
          controls.checkTransmission(true));
   }

   @Test
   public void sendsDiagnosticMessage() {
      when(client.receive()).then(invocation -> {
//         Thread.sleep(1000);
         return UUID.randomUUID().toString();
      });
      controls.checkTransmission(true);
      verify(client).send("AT#UD");
      System.out.println(controls.getDiagnosticInfo());
   }

   @Captor
   private ArgumentCaptor<ClientConfiguration> configCaptor;

   @Test
   public void captureTheArguments() {
      when(client.getOnlineStatus()).thenReturn(true);
      controls.checkTransmission(true);

      verify(client).configure(configCaptor.capture());

      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
   }
}

//class FakeTelemetryClient extends TelemetryClient {
//   @Override
//   public String receive() {
//      return UUID.randomUUID().toString();
//   }
//}




// Agenda:
//- Stubbing Methods - when.thenReturn
//- Mocking Methods - verify
//- Stubbing Dynamic Responses - thenAnswer
// - lenient stubbing are not offended if you dont use them
//- Retries and Delays - thenReturn(true,false)
//- Argument Matching - eq/ any(), ArgCaptor
//- Spies (partial mocks)
//- Mocking Statics
//- Integration with JUnit4/5



// static PRO:
// + easy to call
// + easier to craft PURE function
// + not OOP

// instance PRO
// + injected dep polymorphism @Aspects
// + easier to test due to Dep Injection


//@Service
//class AnotherClass {
////   @Transactional
////   @Cacheable
////   @PreAu
//   public void method() {
//
//   }
//
//}
//@Service
//class MyService {
//   private final AnotherClass o;
//
//   public void method() {
//      o.stuff();
//   }
//}

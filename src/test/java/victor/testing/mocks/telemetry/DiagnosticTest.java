package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Diagnostic.DIAGNOSTIC_CHANNEL_CONNECTION_STRING;

@ExtendWith(MockitoExtension.class) // the extension is in charge to initialize the test class
@MockitoSettings(strictness = Strictness.LENIENT) // default is STRICT // DONT!
public class DiagnosticTest {
  @Mock
  Client mockClient;// = mock(Client.class); // using mock() method makes all stubbing lenient by default. unlike @Mock
  @InjectMocks
  Diagnostic sut;// = new Diagnostic(mockClient);
  @BeforeEach
  final void before() {
    // it's a conscious decision to allow the stubbed meethod NOT be called by some @Test bellow
    when(mockClient.getVersion()).thenReturn("ver");
    when(mockClient.getOnlineStatus()).thenReturn(true);
    when(mockClient.receive()).thenReturn("infoAFHAUIFYA&");
  }
    //    lenient().when(featureService.isFlagActive(MY_FEATURE2312)).thenReturn(true); // eg

  // DOn't use annotations iff you want to pass a real object to the SUT as a dependency
  // very good practice to test more than a single class with "social unit tests" vs isolated unit tests
  //  Diagnostic sut = new Diagnostic(mockClient, new MyRealMapper()); // in the field initialization
  //  Diagnostic sut = new Diagnostic(mockClient, configProperty); // @Value in prod code taken via constructor

  @Test
  void disconnects() {
    sut.checkTransmission(true);

    verify(mockClient).disconnect(true);
  }
  @Test
  void connects() {
    when(mockClient.getOnlineStatus()).thenReturn(false, true);

    sut.checkTransmission(true);

    verify(mockClient).connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
  }

  @Test
  void throwsWhenCannotConnect() {
    when(mockClient.getOnlineStatus()).thenReturn(false);

    assertThatThrownBy(()-> sut.checkTransmission(true))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Unable to connect.");
  }

  @Test
  void receivesDiagnosticInfo() {
    sut.checkTransmission(true);

    verify(mockClient).receive();
    // Only verify NOT pure function calls:
    // - they might do side effects
    // - they might return different results the 2nd time for the same inputs
    // - they might take time (network roundtrip, DB query, etc)
    // tl;dr
    // A) if that is a COMMAND (void method, eg sendKafka, repo.save()) -> verify it was called
    // B) if that is a QUERY (supposed to bring data) -> does network -> verify()
    assertThat(sut.getDiagnosticInfo()).isEqualTo("infoAFHAUIFYA&");
//    verify(mockClient,never()).reportFraud(); // afraid of terrible conseq of an  API
  }

  // TODO would I add the getDiagnosticInfo getter if
  //   it weren't there ? just for testing ?
  // 👌NO, if I would find another way to use that state that I assigned
  //    don't break encapsulation for testing
  // imagine a class of 2000 lines of code with 7 mutable fields. You want to test a 120 lines method
  //    Would I keep the shit encapsulated, or would I break encapsulation for testing
  //   and add 7 getters for the 7 fields YES <=> facing terrible legacy

  @Test
  void configuresCorrectlyTheClient() {
    sut.checkTransmission(true);

    // cherry pick and test 1 attribute only in a complex param pobject
    verify(mockClient).configure(argThat(c->c.getAckMode() == AckMode.NORMAL));

    var configCaptor = ArgumentCaptor.forClass(ClientConfiguration.class);
    verify(mockClient).configure(configCaptor.capture());
    ClientConfiguration config = configCaptor.getValue();
    assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
    assertThat(config.getSessionId())
            .startsWith("VER-")
            .hasSize("ver-b3ba1c7d-f426-4272-bd3f-19d087bd56df".length());
    // USE THIS every time you say now() in prod code - 99% of time
    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
  }


  @Test
  void testTheConfigureDirectly() {
    ClientConfiguration config = sut.createConfig("ver");

    assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
    assertThat(config.getSessionId())
            .startsWith("VER-")
            .hasSize("ver-b3ba1c7d-f426-4272-bd3f-19d087bd56df".length());
    assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
  }


  // TODO how much Captors is too much => your design sucks
}


// you parse a file that contains a date.
// that date should be at most 10 days before now() = ARITHMETIC ON TIME
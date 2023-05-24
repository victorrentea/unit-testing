//package victor.testing.mocks.telemetry;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedConstruction;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class DiagnosticSpecialTest {
//	@Mock
//	Client client;
//	@InjectMocks
//  Diagnostic diagnostic;
//
//  @Test
//  void emptyDiagnosticInitially() {
//    assertThat(diagnostic.getDiagnosticInfo()).isEqualTo("");
//  }
//
//  @Test
//  void throwWhenUnableToConnect() {
//    doReturn(false).when(client).getOnlineStatus();
//
//    assertThatThrownBy(() -> diagnostic.checkTransmission(true))
//        .isInstanceOf(IllegalStateException.class)
//        .hasMessage("Unable to connect.");
//  }
//
//  @Test
//  void happyFlow() { // test everything you can on one path
//    // + less test code with the same power
//    // - not specific name
//    String message = "Something simple";
//    when(client.receive()).thenReturn(message);
//    when(client.getOnlineStatus()).thenReturn(true);
//
//    diagnostic.checkTransmission(false);
//
//    assertThat(diagnostic.getDiagnosticInfo()).isEqualTo(message);
//    verify(client).disconnect(false);
//    verify(client).send(Client.DIAGNOSTIC_MESSAGE);
//    verify(client).configure(any(Client.ClientConfiguration.class));
//  }
//
//  @Test
//  @DisplayName("コードを書く ジャバは強力だ オブジェクト指向")
//  void shouldContainVersionInSession() {
//    String wisdom = "コードを書く ジャバは強力だ オブジェクト指向";
//    when(client.getVersion()).thenReturn(wisdom);
//    when(client.getOnlineStatus()).thenReturn(true);
//    try (MockedConstruction<Client.ClientConfiguration> mock = mockConstruction(Client.ClientConfiguration.class)) {
//      diagnostic.checkTransmission(true);
//
//      List<Client.ClientConfiguration> createdConfigurations = mock.constructed();
//      Client.ClientConfiguration mockedConfiguration = createdConfigurations.get(0);
//      assertThat(createdConfigurations.size()).isEqualTo(1);
//      verify(mockedConfiguration).setSessionId(matches(wisdom + ".*"));
//      verify(mockedConfiguration).setAckMode(any(Client.ClientConfiguration.AckMode.class));
//      verify(mockedConfiguration).setSessionStart(any(LocalDateTime.class));
//    }
//  }
//
//}

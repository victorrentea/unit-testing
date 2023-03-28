package victor.testing.mocks.telemetry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Diagnostic.DIAGNOSTIC_CHANNEL_CONNECTION_STRING;

@ExtendWith(MockitoExtension.class) // the extension is in charge to initialize the test class
public class DiagnosticTest {
  @Mock
  Client mockClient;
  @InjectMocks
  Diagnostic sut;

  @Test
  void disconnects() {
    when(mockClient.getOnlineStatus()).thenReturn(true);

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

    Assertions.assertThatThrownBy(()-> sut.checkTransmission(true))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Unable to connect.");
  }
}

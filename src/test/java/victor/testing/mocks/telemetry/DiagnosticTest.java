package victor.testing.mocks.telemetry;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DiagnosticTest {
  Diagnostic diagnostic;

  @BeforeEach
  public void init() {
    diagnostic = new Diagnostic();
    diagnostic.setTelemetryClient(new Client());
  }

  @Test
  public void checkDiagnosticInfo() {
    assertThat(diagnostic.getDiagnosticInfo())
        .isEqualTo("");
  }

  @Test
  public void throwsForDisconnect() {
    boolean force = true;
    assertThatThrownBy(() -> diagnostic.checkTransmission(force))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Performs some external remote call impossible/not desirable to call from automated tests.");
  }

}

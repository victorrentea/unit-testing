package victor.testing.mocks.telemetry;

import jdk.jshell.Diag;
import org.junit.jupiter.api.Test;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.*;

public class DifferentTestClassForConfigureClient {



  @Test
  void testJustTheConfiguration() {

    Client.ClientConfiguration config = Diagnostic.configureClient("ver");

    assertThat(config.getAckMode()).isEqualTo(NORMAL);
    assertThat(config.getSessionStart())
        .isCloseTo(now(), byLessThan(1, SECONDS));
    assertThat(config.getSessionId())
        .startsWith("ver-");
  }
}

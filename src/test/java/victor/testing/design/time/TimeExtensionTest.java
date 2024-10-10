package victor.testing.design.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.Assertions.assertThat;

public class TimeExtensionTest {

  @RegisterExtension
  TimeExtension timeExtension = new TimeExtension("2023-10-10");

  @Test
  void simple() {
    assertThat(LocalDate.now()).isEqualTo("2023-10-10");
  }

  @Test
  void inAnotherThreadNo() throws ExecutionException, InterruptedException {
    LocalDate inAnotherThread = supplyAsync(() -> LocalDate.now()).get();
    assertThat(inAnotherThread).isNotEqualTo("2023-10-10");
  }

  @Test
  void canUseOtherStaticMethods() throws ExecutionException, InterruptedException {
    LocalDate parsed = LocalDate.parse("2021-01-01");
    assertThat(parsed).isEqualTo("2021-01-01");
  }
}

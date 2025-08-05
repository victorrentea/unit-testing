package victor.testing.design.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.Assertions.assertThat;

class TimeExtensionTest {

  @RegisterExtension
  TimeExtension timeExtension = new TimeExtension("2023-12-25");

  private static LocalDate testedCode() {
    return LocalDate.now();
  }

  @Test
  void controlTime() {
    assertThat(testedCode()).isEqualTo("2023-12-25");
  }

  @Test
  void canChangeTimeDuringTest() {
    timeExtension.setCurrentDate(LocalDate.parse("2023-12-31"));
    assertThat(testedCode()).isEqualTo("2023-12-31");
  }

  @Test
  void otherStaticMethodsAreNotMocked() throws ExecutionException, InterruptedException {
    LocalDate parsed = LocalDate.parse("2021-01-01");
    assertThat(parsed).isEqualTo("2021-01-01");
  }

  @Test
  void threadLocal() throws ExecutionException, InterruptedException {
    LocalDate inAnotherThread = supplyAsync(() -> LocalDate.now()).get();
    assertThat(inAnotherThread).isNotEqualTo("2023-12-25");
  }
}

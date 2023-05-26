package victor.testing.spring.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)// force a class in single thread.
public class TestClassThatHasToRunSingleThreaded {
  static int x; // shared mutable state

  @Test
  void one() throws InterruptedException {
    x = 1;
    Thread.sleep(500);
    assertThat(x).isEqualTo(1);
  }

  @Test
  void two() throws InterruptedException {
    x = 2;
    Thread.sleep(500);
    assertThat(x).isEqualTo(2);
  }
}

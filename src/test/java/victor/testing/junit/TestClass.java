package victor.testing.junit;

import org.junit.jupiter.api.Test;

public class TestClass {
  @Test
  void test1() {
    System.out.println("Fast");
  }

  @Test
  void test2() throws InterruptedException {
    System.out.println("Slow");
    Thread.sleep(1000);
  }
}

class T2 {
  @Test
  void experiment() {
    System.out.println("Experiment");
  }
}
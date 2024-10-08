package victor.testing.tennis;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class CucumberTimedTag {
  private long t0;

  @Before(value = "@timed")
  public void startTimer() {
    t0 = System.currentTimeMillis();
  }

  @After(value = "@timed")
  public void printTime() {
    System.out.println("Scenario took: " + (System.currentTimeMillis() - t0) + "ms");
  }
}

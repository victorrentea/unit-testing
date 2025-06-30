package victor.testing.design.time;

import org.springframework.stereotype.Component;

@Component
public class AnotherClass {
  private final TimeLogic2 timeLogic2;

  public AnotherClass(TimeLogic2 timeLogic2) {
    this.timeLogic2 = timeLogic2;
  }

  public void method() {
    timeLogic2.isFrequentBuyer(1, null);

  }
}

package victor.testing.design.time;

import java.time.LocalDate;

public class AnotherClassInProdShouldNotSeeThe_method {
  private final TimeBasedLogic timeBasedLogic;

  public AnotherClassInProdShouldNotSeeThe_method(TimeBasedLogic timeBasedLogic) {
    this.timeBasedLogic = timeBasedLogic;
  }

  public void method() {
    timeBasedLogic.isFrequentBuyer_(1, LocalDate.now());
  }
}

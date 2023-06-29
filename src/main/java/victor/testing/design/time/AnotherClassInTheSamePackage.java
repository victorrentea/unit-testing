package victor.testing.design.time;

import java.time.LocalDate;

public class AnotherClassInTheSamePackage {
  private final TimeLogic3 logic3;

  public AnotherClassInTheSamePackage(TimeLogic3 logic3) {
    this.logic3 = logic3;

  }

  public void method() {
    boolean b = logic3.isFrequentBuyer(1);
//    boolean b = logic3.isFrequentBuyerInternal(1, LocalDate.now());
    // this method should not be called from prodiucton
// I only made it pacjkage for tests
  }
}

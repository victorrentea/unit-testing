package victor.testing.design.time;

import java.time.LocalDate;

public class AltaClasa {
  private final TimeLogic4 timeLogic4;

  public AltaClasa(TimeLogic4 timeLogic4) {
    this.timeLogic4 = timeLogic4;
  }

  public void t() {
    // apel ilegal catre o functie facuta package private doar pt tests
    timeLogic4.isFrequentBuyer(1, LocalDate.now().plusMonths(1));
  }
}

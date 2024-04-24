package victor.testing.design.spy;

import static java.time.LocalDate.now;

public class TestData {
  public static Order oldOrder() {
    return new Order()
        .setCreationDate(now().minusMonths(2));
  }
}

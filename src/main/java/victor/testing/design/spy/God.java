package victor.testing.design.spy;

import lombok.Data;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

public class God {
  public String high(Order order) {
    low(order);
    // complexity requiring 5+ tests
    if (order.getPaymentMethod() == CARD) {
      return "bonus";
    }
    return "regular";
  }

  private void low(Order order) {
    // complexity requiring 5+ tests
    if (order.getCreationDate().isBefore(now().minusMonths(1))) {
      throw new IllegalArgumentException("Order too old");
    }
    // more stuff to test
  }
}


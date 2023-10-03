package victor.testing.design.spy;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;

import static java.time.LocalDate.now;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

@RequiredArgsConstructor
public class BigService {
  private final LowService low;
  public String high(Order order) { // f()
    low.low(order);
    if (order.getPaymentMethod() == CARD) {
      return "bonus";
    }
    // complexity requiring 5+ tests
    return "regular";
  }

}
class LowService {
  // i am deliberately breaking encapsulation
  // for tests only. But NO OTHER PROD class should
  // call this func
  public void low(Order order) { //g()
    if (order.getCreationDate().isBefore(now().minusMonths(1))) {
      throw new IllegalArgumentException("Order too old");
    }
    // complexity requiring 7+ tests
  }
}


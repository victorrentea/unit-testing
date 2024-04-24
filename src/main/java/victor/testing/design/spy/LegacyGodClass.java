package victor.testing.design.spy;

import lombok.Data;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

public class LegacyGodClass {
   private final LowLevel lowLevel;

  public LegacyGodClass(LowLevel lowLevel) {
    this.lowLevel = lowLevel;
  }

  public String high(Order order) { // ... level logic
      lowLevel.low(order);
      // complexity requiring 5+ tests
      if (order.getPaymentMethod() == CARD) {
         return "bonus";
      }
      return "regular";
   }

}
// split the class by layers of abstraction
class LowLevel {
   public void low(Order order) {  // low-level detail
      // complexity requiring 5+ tests
      if (order.getCreationDate().isAfter(now()
          .minusMonths(1))) {
         throw new IllegalArgumentException("Order too recent");
      }
      // more stuff to test
   }
}

@Data
class Order {
   enum PaymentMethod {
      CARD, CASH_ON_DELIVERY
   }
   private LocalDate creationDate;
   private PaymentMethod paymentMethod;
}
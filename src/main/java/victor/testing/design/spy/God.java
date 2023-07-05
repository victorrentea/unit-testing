package victor.testing.design.spy;

import lombok.Data;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

public class God {
   private final Low low;

   public God(Low low) {
      this.low = low;
   }

   public String high(Order order) {
      low.low(order);
      // complexity requiring 5+ tests
      if (order.getPaymentMethod() == CARD) {
         return "bonus";
      }
      return "regular";
   }

}
class Low {
   void low(Order order) { // Package Protected
      // complexity requiring 7+ tests
      if (order.getCreationDate().isAfter(now().minusMonths(1))) {
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
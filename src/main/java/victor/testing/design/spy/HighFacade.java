package victor.testing.design.spy;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

@RequiredArgsConstructor
public class HighFacade {
   private final Low low;
   public String high(Order order) {
      low.low(order);
      // complexity requiring 5+ tests
      if (order.getPaymentMethod() == CARD) {
         return "bonus";
      }
      return "regular";
   }
   // 100% MUTATION-TESTING COVERAGE ⭐️⭐️⭐️⭐️⭐️⭐️
   // you enlarge the public api of this class JUST for test
   // tomorrow another PROD class will call this.
   // BUT THIS function was never intended to be called from outside of this class
}
class Low {

   public void low(Order order) { // Package Protected
      // complexity requiring 5+ tests
      if (order.getCreationDate().isAfter(now().minusMonths(1))) {
         throw new IllegalArgumentException("Order too old");
      }
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
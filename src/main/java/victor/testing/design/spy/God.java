package victor.testing.design.spy;

import com.google.common.annotations.VisibleForTesting;
import lombok.Data;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

public class God {
   // 1] you test through the public method
   public String high(Order order) {
      low(order);
      // complexity requiring 5+ tests
      if (order.getPaymentMethod() == CARD) {
         return "bonus";
      }
      return "regular";
   }

   // 2] open this method for testing: make it public
   // - break the class' encapsulation; risk: someone else might call it
   // solution: package protected + @VisibleForTesting
   @VisibleForTesting
   void low(Order order) { // Package Protected
      // complexity requiring 5+ tests
      if (order.getCreationDate().isBefore(now().minusMonths(1))) {
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
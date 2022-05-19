package victor.testing.designhints.spy;

import lombok.Data;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static victor.testing.designhints.spy.Order.PaymentMethod.CARD;

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
   public void low(Order order) {
      System.out.println("in the low method");
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
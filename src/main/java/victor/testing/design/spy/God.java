package victor.testing.design.spy;

import com.google.common.annotations.VisibleForTesting;
import lombok.Data;
import org.mockito.Spy;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

public class God { // large class called directly from Controller/MQ listener
   public String high(Order order) {
      low(order);
      // complexity requiring 5+ tests
      if (order.getPaymentMethod() == CARD) {
         return "bonus";
      }
      return "regular";
   }

   // 100% mutation-level covered
   public void low(Order order) {
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
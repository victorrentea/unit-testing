package victor.testing.design.spy;

import com.google.common.annotations.VisibleForTesting;
import lombok.Data;
import org.mockito.Spy;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

public class God {
   public String high(Order order) { // freza: 500 lei la baiatu
      low(order);
      // complexity requiring 5+ tests
      if (order.getPaymentMethod() == CARD) {
         return "bonus";
      }
      return "regular";
   }

   void low(Order order) { // TESTATA! ciorapii
      // complexity requiring 5+ tests
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
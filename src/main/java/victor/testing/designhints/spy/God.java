package victor.testing.designhints.spy;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static victor.testing.designhints.spy.Order.PaymentMethod.CARD;
@RequiredArgsConstructor
public class God {
   private final LowLevel lowLevel;
   public String high(Order order) {
      lowLevel.low(order);
      // complexity requiring 5+ tests
      if (order.getPaymentMethod() == CARD) {
         return "bonus";
      }
      return "regular";
   }

}

class LowLevel {

   public void low(Order order) {
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
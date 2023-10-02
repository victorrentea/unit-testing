package victor.testing.design.spy;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Order {
  enum PaymentMethod {
    CARD, CASH_ON_DELIVERY
  }
  private LocalDate creationDate;
  private PaymentMethod paymentMethod;
}

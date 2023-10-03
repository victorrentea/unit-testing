package victor.testing.design.spy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

class BigServiceTest {

  @Test
  void high() { // + 5 more tests like this
    Order order = new Order()
        .setPaymentMethod(CARD)
        .setCreationDate(LocalDate.now());
    String result = new BigService().high(order);
    assertThat(result).isEqualTo("bonus");
  }


  @Test
  void low() { // +7 more tests (pretend)
    LocalDate longAgo = LocalDate.now().minusMonths(2);
    Order order = new Order().setCreationDate(longAgo);

    Assertions.assertThatThrownBy(() -> new BigService().low(order))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Order too old");
  }
}
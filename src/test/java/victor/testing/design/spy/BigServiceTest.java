package victor.testing.design.spy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

@ExtendWith(MockitoExtension.class)
class BigServiceTest {
  @InjectMocks
  BigService bigService;
  @Mock
  LowService low;

  @Test
  void high() { // + 5 more tests like this
    Order order = new Order()
        .setPaymentMethod(CARD);
    String result = bigService.high(order);
    assertThat(result).isEqualTo("bonus");
  }

}
class LowTest {

  @Test
  void low() { // +7 more tests (pretend)
    LocalDate longAgo = LocalDate.now().minusMonths(2);
    Order order = new Order().setCreationDate(longAgo);

    Assertions.assertThatThrownBy(() -> new LowService().low(order))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Order too old");
  }
}
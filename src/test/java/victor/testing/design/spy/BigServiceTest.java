package victor.testing.design.spy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BigServiceTest {



  @Test
  void low() { // +7 more tests (pretend)
    LocalDate longAgo = LocalDate.now().minusMonths(2);
    Order order = new Order().setCreationDate(longAgo);

    Assertions.assertThatThrownBy(() -> new BigService().low(order))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Order too old");
  }
}
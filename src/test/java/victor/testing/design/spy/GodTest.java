package victor.testing.design.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

@ExtendWith(MockitoExtension.class)
class GodTest {

   @Spy
   God god;

   @Test
   void high() { // + 5 more tests like this
//      doNothing().when(god).low(any());
      Order order = new Order().setPaymentMethod(CARD);

      String result = god.high(order);

      assertThat(result).isEqualTo("bonus");
   }
//
   @Test
   void low() { // + 5 more tests like this
      Order oldOrder = new Order().setCreationDate(now().minusMonths(2));
      assertThatThrownBy(() -> god.low(oldOrder))
          .hasMessageContaining("old");
   }
}
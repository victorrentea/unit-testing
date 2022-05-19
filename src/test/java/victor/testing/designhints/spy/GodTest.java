package victor.testing.designhints.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.designhints.spy.Order.PaymentMethod.CARD;

@ExtendWith(MockitoExtension.class)
class GodTest {

   @InjectMocks
   God god;
   @Mock
   Low low;

   @Test
   void high() { // + 5 more tests like this
//     when(low.low(any())).t;
      Order order = new Order().setPaymentMethod(CARD)/*.setCreationDate(now())*/;

      String result = god.high(order);

      verify(low).low(order);
      assertThat(result).isEqualTo("bonus");
   }

}
class LowTest {

   @Test
   void low() { // + 5 more tests like this
      Order oldOrder = new Order().setCreationDate(now().minusMonths(2));
      assertThatThrownBy(() -> new Low().low(oldOrder))
          .hasMessageContaining("old");
   }
}

package victor.testing.design.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

@ExtendWith(MockitoExtension.class)
class HighFacadeTest {
   @Mock
   Low low;
   @InjectMocks
   HighFacade highFacade;

   @Test
   void high() { // + 5 more tests like this
      Order order = new Order().setPaymentMethod(CARD);

      String result = highFacade.high(order);

      assertThat(result).isEqualTo("bonus");
   }
//

}

class LowTest {
   @Test
   void low() { // + 5 more tests like this
      Order oldOrder = new Order().setCreationDate(now().minusMonths(0));
      assertThatThrownBy(() -> new Low().low(oldOrder))
          .hasMessageContaining("old");
   }
}
package victor.testing.design.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

@ExtendWith(MockitoExtension.class)
class GodSpyTest {

   @Spy
   God god;

//   @Test
//   void high() { // + 5 more tests like this
//      doNothing().when(god).low(any()); // let's stop the high() method under test
//      // from calling low() since we already tested that
//      Order order = new Order().setPaymentMethod(CARD);
//
//      String result = god.high(order);
//
//      assertThat(result).isEqualTo("bonus");
//   }
//
//   @Test
//   void lowTooRecent() { // + 5 more tests like this
//      Order oldOrder = new Order().setCreationDate(now());
//      assertThatThrownBy(() -> god.low(oldOrder))
//          .hasMessageContaining("recent");
//   }
//   @Test
//   void low() { // + 5 more tests like this
//      Order oldOrder = new Order().setCreationDate(now().minusMonths(2));
//      god.low(oldOrder);
//      // more assert/verify ..
//   }
}
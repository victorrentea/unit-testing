package victor.testing.design.spy;

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
import static victor.testing.design.spy.Order.PaymentMethod.CARD;

@ExtendWith(MockitoExtension.class)
class GodTest {

   @InjectMocks
   God god;
   @Mock
   Low low;

   @Test
   void high() { // + 5 more tests like this
//      doNothing().when(god).low(any());
      // from calling low() since we already tested that
      Order order = new Order().setPaymentMethod(CARD);
//          .setCreationDate(now().minusMonths(2)); // OBVIOUS WHY!??! Not fair!! I've tested low() already
      String result = god.high(order);

      assertThat(result).isEqualTo("bonus");
   }

//   @Test
//   void lowTooRecent() { // + 5 more tests like this
//      Order oldOrder = new Order().setCreationDate(now());
//      assertThatThrownBy(() -> god.low(oldOrder))
//          .hasMessageContaining("recent");
//   }
}
class LowTest {
   @Test
   void low() { // + 7 more tests like this
      Order oldOrder = new Order().setCreationDate(now().minusMonths(2));
      new Low().low(oldOrder);
      // more assert/verify ..
   }
}

// WHEN to do it ? when NOT to?


// HOW to motivate people to do it?
// hard! perhaps you need some prep-refactoring for the BIG CHANGES.
//   Talking about such subtle changes in terrible codebases won't work.
// Make it fun. Work together. Mob/Pair
// Recruit the right people.
// Rotate project.

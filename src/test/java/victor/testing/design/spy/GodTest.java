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
   God god;// = new God();

   // TODO test high method
   @Test
   void experiment() {
      Order order = new Order();
//          .setCreationDate(now());// dar apoi + inca 10 inputuri pentru ca low() sanu creape
      doNothing().when(god).low(any()); // = mock partial: in clasa de testat mockuiesc metode
      // sau pot bloca apelul de la high->low cu mockuri partiale (aka SPY)
      String r = god.high(order);
      assertThat(r).isEqualTo("regular");
   }

   @Test
   void lowThrowsIfOrderTooRecent() { // + 15 more tests like this
      Order order = new Order().setCreationDate(now());
      assertThatThrownBy(() -> god.low(order))
          .hasMessageContaining("recent");
   }

   @Test
   void low() {
      Order oldOrder = new Order().setCreationDate(now().minusMonths(2));
//      god.low(oldOrder);
   }// + 5 more tests on low
}
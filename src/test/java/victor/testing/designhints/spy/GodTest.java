package victor.testing.designhints.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.designhints.spy.Order.PaymentMethod.CARD;

@ExtendWith(MockitoExtension.class)
class GodTest {

   @Mock
   LowLevel low;
   @InjectMocks // forbidden in good design
   God god;

   @Test
   void high() { // + 5 more tests like this
//      Mockito.doNothing().when(god).low(any());
      Order order = new Order().setPaymentMethod(CARD);

      String result = god.high(order);

      assertThat(result).isEqualTo("bonus");
   }


}
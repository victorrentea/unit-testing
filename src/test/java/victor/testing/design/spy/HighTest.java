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
class HighTest {
   @Mock
   Low low;
   @InjectMocks //2] partial mock : never ; either break the tested class or pass more input/mocks in
   High target;
   @Test
   void high() { // + 5 more tests like this
//      doNothing().when(god).low(any());
//      doReturn("shit").when(god).low(any());

      String actual = target.high(new Order()
          .setPaymentMethod(CARD)
//          .setCreationDate(now())//1] WHY>>!!> #LIFE IS NOT FAIR
      );

      assertThat(actual).isEqualTo("bonus");
   }
   // is it fair to have tests on public method FAIL because of the private method?
   // REMEMBER: I already fully tested the private one directly below
}
class LowTest{

   @Test
   void testLowDirectly() {
      assertThatThrownBy(()-> new Low().low(new Order().setCreationDate(now().minusMonths(2))))
          .isInstanceOf(IllegalArgumentException.class);

   }
}
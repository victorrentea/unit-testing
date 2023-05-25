package victor.testing.design.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import wiremock.com.google.common.reflect.Reflection;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.design.spy.Order.PaymentMethod.CARD;
import static victor.testing.design.spy.Order.PaymentMethod.CASH_ON_DELIVERY;

class GodTest {

   @Test
   void high() { // + 5 more tests like this
      String actual = new God().high(new Order().setPaymentMethod(CARD));

      assertThat(actual).isEqualTo("bonus");
   }
   @Test
   void high2() { // + 5 more tests like this
      String actual = new God().high(new Order().setPaymentMethod(CASH_ON_DELIVERY));

      assertThat(actual).isEqualTo("regular");
   }
   
   @Test
   void testLowDirectly() {
//     refletion.callprivateMethod("low")
   }
}
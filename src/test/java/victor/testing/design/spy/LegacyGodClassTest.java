package victor.testing.design.spy;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class LegacyGodClassTest {

   LegacyGodClass legacyGodClass = new LegacyGodClass(){
      @Override
      void low(Order order) {
      }
   };

   // TODO test high method

   @Test
   void highTest() { // + 5 more tests like this
      Order order = new Order()
          .setPaymentMethod(Order.PaymentMethod.CARD);
      String result = legacyGodClass.high(order);

      assertThat(result).isEqualTo("bonus");
   }

   @Test
    void highTestRegular() {
        Order order = new Order()
            .setPaymentMethod(Order.PaymentMethod.CASH_ON_DELIVERY);
        String result = legacyGodClass.high(order);

        assertThat(result).isEqualTo("regular");
    }



   @Test
   void low() {
      LocalDate twoMonthsAgo = now().minusMonths(2);
      Order oldOrder = new Order().setCreationDate(twoMonthsAgo);
      legacyGodClass.low(oldOrder);
   }// + 5 more tests on low

   @Test
   void lowThrowsForRecentOrder() {
      LocalDate twoMonthsAgo = now().minusDays(1);
      Order oldOrder = new Order().setCreationDate(twoMonthsAgo);
      //legacyGodClass.low(oldOrder);
      assertThatThrownBy(() -> legacyGodClass.low(oldOrder))
          .hasMessageContaining("recent");
   }// + 5 more tests on low
}
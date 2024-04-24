package victor.testing.design.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LegacyGodClassTest {

  // temporary tests
  @InjectMocks // too to use to write "FREEZE-TESTS" on legacy code to refactor a bit of it out
      LegacyGodClass legacyGodClass;// = spy(new LegacyGodClass());

  @Mock
  LowLevel lowLevel;

  @Test
  void highTest() {
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
}
class LowLevelTest {


  private LowLevel lowLevel = new LowLevel();

  @Test
   void low() {
      Order oldOrder = TestData.oldOrder();
      lowLevel.low(oldOrder);
   }// + 5 more tests on low

   @Test
   void lowThrowsForRecentOrder() {
      LocalDate twoMonthsAgo = now().minusDays(1);
      Order oldOrder = new Order().setCreationDate(twoMonthsAgo);
      //legacyGodClass.low(oldOrder);
      assertThatThrownBy(() -> lowLevel.low(oldOrder))
          .hasMessageContaining("recent");
   }// + 5 more tests on low
}
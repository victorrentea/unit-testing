//package victor.testing.design.spy;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static java.time.LocalDate.now;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static victor.testing.design.spy.Order.PaymentMethod.CARD;
//
//class GodTest {
//
//   God god = spy(new God());
//
//   // solutia 1: pleci de la un order standard luat din TestData
//   @Test
//   public void high() {
//      Order order = new Order().setPaymentMethod(CARD);
//      doNothing().when(god).low(any());
////      doNothing().when(god).low(order);
//      doNothing().when(god).low(argThat(o -> o.getPaymentMethod() == CARD));
//      String result = god.high(order);
//      assertThat(result).isEqualTo("bonus");
//   }
//
//   // TODO test high method
//
//   @Test
//   void lowThrowsIfOrderTooRecent() { // + 5 more tests like this
//      Order order = new Order().setCreationDate(now());
//      assertThatThrownBy(() -> god.low(order)).hasMessageContaining("recent");
//   }
//
//   @Test
//   void low() { // x 5
//      Order oldOrder = new Order().setCreationDate(now().minusMonths(2));
//      god.low(oldOrder);
//   }// + 5 more tests on low
//}
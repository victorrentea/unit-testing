package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeLogic5Test {
  OrderRepo orderRepoMock = mock(OrderRepo.class);
  TimeLogic5 target = new TimeLogic5(orderRepoMock);

  @Test
  void isFrequentBuyer() {
    LocalDate today = now(TimeProvider.clock);
    LocalDate oneWeekAgo = today.minusWeeks(1);
    Order order = new Order().setTotalAmount(130d);
    when(orderRepoMock.findByCustomerIdAndCreatedOnBetween(
        13, oneWeekAgo, today))
        .thenReturn(List.of(order));

    assertThat(target.isFrequentBuyer(13)).isTrue();
  }
}
package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@Powermock
class TimeLogic3Test {
  OrderRepo orderRepoMock = mock(OrderRepo.class);
  TimeLogic3 target = new TimeLogic3(orderRepoMock);

  @Test
//  @Disabled("flaky, time-based")
  void isFrequentBuyer() {
    LocalDate today = parse("2023-01-08");
    LocalDate oneWeekAgo = parse("2023-01-01");
    Order order = new Order().setTotalAmount(130d);
    when(orderRepoMock.findByCustomerIdAndCreatedOnBetween(
        13, oneWeekAgo, today))
        .thenReturn(List.of(order));

    try(var staticMock = Mockito.mockStatic(LocalDate.class)) {
      staticMock.when(() -> LocalDate.now()).thenReturn(today);
      var result = target.isFrequentBuyer(13);
      assertThat(result).isTrue();
    }

  }
}
// Ways to control time from tests:
// - inject a Clock dependency, pass a fixed Clock from tests (see ClockUtils)
// - pass time as an argument to a package-protected method ("subcutaneous test")
// - mock the static method LocalDate.now()
// - inject an (Mock-able) object wrapping the static call: class TimeProvider { LocalDate today() {return LocalDate.now();} }
//   - variation: Supplier<LocalDate>
//   - variation: UUIDGenerator
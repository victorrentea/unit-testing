package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class TimeLogic4Test {
  OrderRepo orderRepoMock = mock(OrderRepo.class);
  TimeLogic4 target = new TimeLogic4(orderRepoMock);

  @Test
  void isFrequentBuyer() {
    LocalDate today = parse("2023-01-08");
    LocalDate oneWeekAgo = parse("2023-01-01");
    Order order = new Order().setTotalAmount(130d);
    when(orderRepoMock.findByCustomerIdAndCreatedOnBetween(
        13, oneWeekAgo, today))
        .thenReturn(List.of(order));

    var futures = new ArrayList<CompletableFuture<Void>>();
    for (int i = 0; i < 100; i++) {

      CompletableFuture<Void> f = CompletableFuture.runAsync(() -> mmm(today));
      futures.add(f);
    }
    for (CompletableFuture<Void> future : futures) {
      future.join();
    }
    // last resort
  }

  private void mmm(LocalDate today) {
    try(var staticMock = mockStatic(LocalDate.class)) {
      staticMock.when(LocalDate::now).thenReturn(today);
      boolean result = target.isFrequentBuyer(13);
      assertThat(result).isTrue();
    }
  }
}
// Ways to control time from tests:
// - inject a Clock dependency, pass a fixed Clock from tests (see TimeUtils)
// - pass time as an argument to a package-protected method ("subcutaneous test")
// - mock the static method LocalDate.now()
// - inject an (Mock-able) object wrapping the static call: class TimeProvider { LocalDate today() {return LocalDate.now();} }
//   - variation: Supplier<LocalDate>
//   - variation: UUIDGenerator
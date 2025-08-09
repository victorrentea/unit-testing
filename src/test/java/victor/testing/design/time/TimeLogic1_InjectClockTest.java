package victor.testing.design.time;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import victor.testing.tools.ClockTestUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeLogic1_InjectClockTest {
  OrderRepo orderRepoMock = mock(OrderRepo.class);
  Clock clock = ClockTestUtils.fixedDateClock("2023-01-08"); // ⭐️
  TimeLogic1_InjectClock target = new TimeLogic1_InjectClock(orderRepoMock, clock);

  // to replace the real Clock bean in a @SpringBootTest, use a @TestConfiguration
  // limitation: you cannot change the clock during @Test, for subsequent calls from prod

  @Test
  void isFrequentBuyer() {
    LocalDate today = parse("2023-01-08");
    LocalDate oneWeekAgo = parse("2023-01-01");
    Order order = new Order().setTotalAmount(130d);
    when(orderRepoMock.findByCustomerIdAndCreatedOnBetween(
        13, oneWeekAgo, today))
        .thenReturn(List.of(order));

    assertThat(target.isFrequentBuyer(13)).isTrue();
  }
}


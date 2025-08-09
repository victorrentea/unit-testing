package victor.testing.design.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeLogic2_InjectTimeProviderTest {
  @Mock
  OrderRepo orderRepoMock;
  @Mock
  TimeProvider timeProvider;
  @InjectMocks
  TimeLogic2_InjectTimeProvider target;

  @Test
  void isFrequentBuyer() {
    LocalDate today = parse("2023-01-08");
    when(timeProvider.today()).thenReturn(today); // ⭐️
    LocalDate oneWeekAgo = parse("2023-01-01");
    Order order = new Order().setTotalAmount(130d);
    when(orderRepoMock.findByCustomerIdAndCreatedOnBetween(
        13, oneWeekAgo, today))
        .thenReturn(List.of(order));

    assertThat(target.isFrequentBuyer(13)).isTrue();
  }
}
package victor.testing.design.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeLogic4_MockStaticTest {
  @Mock
  OrderRepo orderRepoMock;
  @InjectMocks
  TimeLogic4_Untouched target;

  @Test
  void isFrequentBuyer() {
    LocalDate today = parse("2023-01-08");
    LocalDate oneWeekAgo = parse("2023-01-01");
    Order order = new Order().setTotalAmount(130d);
    when(orderRepoMock.findByCustomerIdAndCreatedOnBetween(
        13, oneWeekAgo, today))
        .thenReturn(List.of(order));
    try (var staticMock = Mockito.mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
      staticMock.when(LocalDate::now).thenAnswer(call -> today); // ⭐️

      boolean frequentBuyer = target.isFrequentBuyer(13);

      assertThat(frequentBuyer).isTrue();
    }
  }
}
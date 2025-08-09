package victor.testing.design.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.tools.TimeExtension;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeLogic5_ExtensionTest {
  public static final LocalDate TODAY = parse("2023-01-08");
  @RegisterExtension
  TimeExtension timeExtension = new TimeExtension(TODAY); // ⭐️
  @Mock
  OrderRepo orderRepoMock;
  @InjectMocks
  TimeLogic4_Untouched target;


  @Test
  void isFrequentBuyer() {
    LocalDate oneWeekAgo = parse("2023-01-01");
    Order order = new Order().setTotalAmount(130d);
    when(orderRepoMock.findByCustomerIdAndCreatedOnBetween(
        13, oneWeekAgo, TODAY))
        .thenReturn(List.of(order));

    assertThat(target.isFrequentBuyer(13)).isTrue();
  }
}
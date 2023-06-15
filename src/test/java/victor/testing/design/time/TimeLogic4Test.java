package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeLogic4Test {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeLogic4 target;

   @Test
   @Disabled("flaky, time-based")
   void isFrequentBuyer() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13,
          parse("2021-09-01"),
          parse("2021-09-08")))
       .thenReturn(List.of(new Order().setTotalAmount(130d)));

      assertThat(target.isFrequentBuyer(13)).isTrue();
   }
}

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
class TimeLogic1Test {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeLogic1 target;

   @Test
   @Disabled("flaky, time-based")
   void isFrequentBuyer() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13,
          parse("2021-09-01"),
          parse("2021-09-08"))).thenReturn(List.of(new Order().setTotalAmount(130d)));

      assertThat(target.isFrequentBuyer(13)).isTrue();
   }
}

// 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
// 2: interface for Clock retrival [general solution] -> **indirection without abstraction**
// 3: inject a Supplier<LocalDate>
// 4: pass time as method arg
// 5: package-protected variant for testing
// 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)

package victor.testing.mocks.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeBasedLogicTest {
   @Mock
   OrderRepo orderRepo;
   @Mock
   TimeProvider timeProvider;
//   @Mock
//   Clock clock; // Instant - BAD: framework ugly grose detail
   @InjectMocks
   TimeBasedLogic target;

   @Test
//   @Disabled("flaky, time-based")
   void isFrequentBuyer() {
      when(timeProvider.today()).thenReturn(parse("2021-12-25"));

      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13, parse("2021-12-18"), parse("2021-12-25"))).thenReturn(List.of(new Order().setTotalAmount(130d)));

      //blame @Boris :)
//      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
//          13, any(), any())).thenReturn(List.of(new Order().setTotalAmount(130d)));

      //copy paste logic from prod - avoid
//      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
//          13, now().minusDays(7), now())).thenReturn(List.of(new Order().setTotalAmount(130d)));

      assertThat(target.isFrequentBuyer(13)).isTrue();

      // 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
      // 2: interface for Clock retrival [general solution] -> **indirection without abstraction**
      // 3: inject a Supplier<LocalDate>
      // 4: pass time as method arg
      // 5: package-protected variant for testing
      // 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)
   }
}
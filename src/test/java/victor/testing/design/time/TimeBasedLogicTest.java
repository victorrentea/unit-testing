package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.domain.Supplier;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeBasedLogicTest {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeBasedLogic target;
   private LocalDateTime now = LocalDateTime.now();


   //   @Mock Clock clock; // exposing some awkward library Intant ZoneId
   @Test
//   @Disabled("flaky, time-based")
   void isFrequentBuyer() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13,
              now.minus(7*23*3600, ChronoUnit.SECONDS),
             now))
        .thenReturn(List.of(new Order().setTotalAmount(130d)));

      // call to tested code
      assertThat(target.isFrequentBuyer(13, now)).isTrue();

      //NO too heavy 3.5y ago 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
      //NO 3: inject a Supplier<LocalDate> - FP maniac version
      // 4: pass time as method arg
      // 5: package-protected variant for testing
      // 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)
   }
}
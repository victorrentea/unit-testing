package victor.testing.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.LocalDate.parse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeBasedLogicTest {
   @Mock
   OrderRepo orderRepo;
//   @Mock
//   ClockAdapter clock;
   @InjectMocks
   TimeBasedLogic target;

   @Test
   @Disabled("flaky, time-based")
   void isFrequentBuyer() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13, parse("2021-09-01"), parse("2021-09-08")))
          .thenReturn(List.of(new Order().setTotalAmount(130d)));

//      when(clock.instant()).thenReturn(Instant.parse("2021-09-08T10:10:10Z"));
//      when(clock.getZone()).thenReturn(ZoneId.systemDefault());
//      when(clock.today()).thenReturn(parse("2021-09-08"));


//      final LocalDate today = parse("2021-09-08");
//      try(MockedStatic<LocalDate> mockDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
//         mockDate.when(() -> LocalDate.now()).thenReturn(today);
//         assertThat(target.isFrequentBuyer(13)).isTrue();
//      }

//      final LocalDate today = parse("2021-09-08");
//      try(MockedStatic<LocalDate> mockDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
//         mockDate.when(() -> LocalDate.now()).thenReturn(today);
//         assertThat(target.isFrequentBuyer(13)).isTrue();
//      }

      target.isFrequentBuyerAsOfTime(13, parse("2021-09-08"));


      // 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
      // 2: interface for Clock retrival [general solution] -> **indirection without abstraction**
      // 3: inject a Supplier<LocalDate>
      // 4: pass time as method arg
      // 5: package-protected variant for testing
      // 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)
   }
}
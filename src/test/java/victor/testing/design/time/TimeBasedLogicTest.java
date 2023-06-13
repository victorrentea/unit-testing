package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeBasedLogicTest {
   public static final LocalDate CHRISTMAS = parse("2023-12-25");
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
//    @Spy
   TimeBasedLogic target;
//   @Mock Clock clock; // #2 very main stream ⭐️

   @Test
//   @Disabled("flaky test, time-based")
   void isFrequentBuyer_staticMocks() {
//      doReturn(parse("2023-12-25")).when(target).hackMe(); // #3 partial mocks DONT!
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13,
          parse("2023-12-18"),
          CHRISTMAS
          // #1
//          now().minusMonths(7),  now()// copy-paste behavior from prod to test = BAD
         ))
      .thenReturn(List.of(new Order().setTotalAmount(130d)));

      // #4 mock static methods (prev done with PowerMock and great pain and sorrow)
      try (MockedStatic<LocalDate> staticMock = Mockito.mockStatic(LocalDate.class)) {
         staticMock.when(LocalDate::now).thenReturn(CHRISTMAS);
         boolean result = target.isFrequentBuyer(13);
         assertThat(result).isTrue();
      }

      // #5 wrap the annoying static method call into an instance object (eg TimeProvider)
      // and inject an instance of TimeProvider that you can then @Mock in your tests

      // #6 "subcutaneous test"

      // 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
      // 2: interface for Clock retrival [general solution] -> **indirection without abstraction**
      // 3: inject a Supplier<LocalDate>
      // 4: pass time as method arg
      // 5: package-protected variant for testing
      // 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)
   }
}
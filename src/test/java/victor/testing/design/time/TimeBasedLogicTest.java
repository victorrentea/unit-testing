package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeBasedLogicTest {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeBasedLogic target;


   //   @Mock Clock clock; // exposing some awkward library Intant ZoneId
   @Test
//   @Disabled("flaky, time-based")
   // mockito-inline deprecated PowerMock
   void isFrequentBuyer() {
      LocalDate christmas = parse("2022-12-25");
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13,
              LocalDate.parse("2022-12-18"),
              christmas))
        .thenReturn(List.of(new Order().setTotalAmount(130d)));


      try (MockedStatic<LocalDate> staticMockThatINeedToClose = mockStatic(LocalDate.class)) {
         staticMockThatINeedToClose.when(LocalDate::now).thenReturn(christmas);
         // here in the try block, on THIS thread any call to now() gets hacked
         // call to tested code
         boolean result = target.isFrequentBuyer(13);
         assertThat(result).isTrue();

         System.out.println(LocalDate.now());
      }
      // after this line, now() returns normally.



      //NO too heavy 3.5y ago 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
      //NO 3: inject a Supplier<LocalDate> - FP maniac version
      // 4: pass time as method arg
      // 5: package-protected variant for testing
      // 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)
   }
}
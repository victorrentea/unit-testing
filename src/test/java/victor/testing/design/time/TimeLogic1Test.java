package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeLogic1Test {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeLogic1 target;
// mockito inline statics
   @Test
   void isFrequentBuyer() {
      LocalDate currentTime = parse("2023-06-23");

      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13,
          parse("2023-06-16"),
          parse("2023-06-23"))).thenReturn(List.of(new Order().setTotalAmount(130d)));

      try (var staticMock = mockStatic(LocalDate.class)) {
         staticMock.when(() -> LocalDate.now()).thenReturn(currentTime);
         boolean result = target.isFrequentBuyer(13);

         assertThat(result).isTrue();
      }

   }
}

// 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
// 2: interface for Clock retrival [general solution] -> **indirection without abstraction**
// 3: inject a Supplier<LocalDate>
// 4: pass time as method arg
// 5: package-protected variant for testing
// 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)

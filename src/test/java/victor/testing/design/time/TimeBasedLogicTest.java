package victor.testing.design.time;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeBasedLogicTest {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeBasedLogic target;
//   @Mock
//   Supplier<LocalDate> dateSupplier;

   @Test
   //@Disabled("flaky, time-based")
   void isFrequentBuyer() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
              13, parse("2023-12-18"), parse("2023-12-25"))).thenReturn(List.of(new Order().setTotalAmount(130d)));

      assertThat(target.isFrequentBuyer(13, parse("2023-12-25"))).isTrue();
   }
      // 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
      // 2: interface for Clock retrival [general solution] -> **indirection without abstraction**
      // 3: inject a Supplier<LocalDate>
      // 4: pass time as method arg
      // 5: package-protected variant for testing
      // 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)


   @Test
   void isFrequentBuyer_cuMockuriStatice() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
              13, parse("2023-12-18"), parse("2023-12-25"))).thenReturn(List.of(new Order().setTotalAmount(130d)));
      LocalDate craciun = parse("2023-12-25");
      // cu ajutorul mockito-inline, poti scrie:

      try(MockedStatic<LocalDate> staticMock = Mockito.mockStatic(LocalDate.class)) {
         staticMock.when(() -> LocalDate.now()).thenReturn(craciun);

         boolean result = target.isFrequentBuyer(13);
         assertThat(result).isTrue();
      }
   }
}
// daca vr3ei sa controlezi timpul
// #1 Supplier<LocalDate>
// #2 Test Subcutanate (package-protected @VisibleForTesting)
// #3 Mockuri statice
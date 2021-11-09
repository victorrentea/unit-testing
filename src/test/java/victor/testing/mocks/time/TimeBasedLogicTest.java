package victor.testing.mocks.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class TimeBasedLogicTest {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeBasedLogic target;

   @Test
//   @Disabled("flaky, time-based")
   void isFrequentBuyer() {

      // NU
//      Order mockOrder = mock(Order.class);
//      when(mockOrder.getTotalAmount()).thenReturn(2d);

//      new Order().setTotalAmount(2d);

      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13, parse("2021-09-01"), parse("2021-09-08")))
          .thenReturn(List.of(new Order().setTotalAmount(130d)));

      // modifici apiul de prod pentru teste
//      boolean result = target.isFrequentBuyer(13, parse("2021-09-08"));

      LocalDate today = parse("2021-09-08");

      try (MockedStatic<LocalDate> mockStatic = mockStatic(LocalDate.class)) {
         mockStatic.when(LocalDate::now).thenReturn(today);
         boolean result = target.isFrequentBuyer(13);

         assertThat(result).isTrue();
      }


      // 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
      // 2: interface for Clock retrival [general solution] -> **indirection without abstraction**
      // 3: inject a Supplier<LocalDate>
      // 4: pass time as method arg
      // 5: package-protected variant for testing
      // 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)
   }
}
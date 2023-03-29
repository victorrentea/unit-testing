package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeBasedLogicTest {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeBasedLogic target;

   @Test
   void isFrequentBuyerSubcutaneous() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13, parse("2023-03-21"), parse("2023-03-28")))
              .thenReturn(List.of(new Order().setTotalAmount(130d)));

      assertThat(target.isFrequentBuyerInternal(13, parse("2023-03-28")))
              .isTrue();

   }

   @Test // #3 mock static
   void isFrequentBuyerMockingStatics() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13, parse("2023-12-18"), parse("2023-12-25")))
              .thenReturn(List.of(new Order().setTotalAmount(130d)));
      LocalDate testTime = parse("2023-12-25");

      try (MockedStatic<LocalDate> staticMock = Mockito.mockStatic(LocalDate.class)) {
         staticMock.when(LocalDate::now).thenReturn(testTime);
         assertThat(target.isFrequentBuyer(13)).isTrue();
      }


   }
}
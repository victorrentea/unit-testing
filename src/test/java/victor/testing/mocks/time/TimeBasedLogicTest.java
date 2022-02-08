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

//   @Mock
//   Clock
   @InjectMocks
   TimeBasedLogic target;

   @Test
   void isFrequentBuyer() {
//      when(timeProvider.today()).thenReturn(parse("2021-12-25"));


      //blame @Boris :)
//      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
//          13, any(), any())).thenReturn(List.of(new Order().setTotalAmount(130d)));

      //copy paste logic from prod - avoid
//      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
//          13, now().minusDays(7), now())).thenReturn(List.of(new Order().setTotalAmount(130d)));

      // here i have to set the current time to chrismas

      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13, parse("2021-12-18"), parse("2021-12-25"))).thenReturn(List.of(new Order().setTotalAmount(130d)));

      LocalDate christmas = parse("2021-12-25");
      try (MockedStatic<LocalDate> staticMock = mockStatic(LocalDate.class)) {
         staticMock.when(LocalDate::now).thenReturn(christmas);

         assertThat(target.isFrequentBuyer(13)).isTrue();
      }
   }
   @Test
   void subcutaneousTest() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13, parse("2021-12-18"), parse("2021-12-25"))).thenReturn(List.of(new Order().setTotalAmount(130d)));
      LocalDate christmas = parse("2021-12-25");

      assertThat(target.isFrequentBuyer(13, christmas)).isTrue();
   }
}
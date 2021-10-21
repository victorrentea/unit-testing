package victor.testing.mocks.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.*;


@ExtendWith({MockitoExtension.class, ClearLocalDate.class})
//@HackuimByecodeulClaseiLocalDate.class)
class TimeBasedLogicTest {
   @Mock
   OrderRepo orderRepo;
   @Mock
   TimeProvider timeProvider;

   @InjectMocks
   TimeBasedLogic target;


   @Test
   void test() {
      // e perfect testul asta :
      Order order = target.celeMaiMulteMetode();
      assertThat(order.getCreatedOn()).isNotNull();
      assertThat(order.getCreatedOn()).isCloseTo(now(), byLessThan(1, ChronoUnit.MINUTES));
   }

   @org.junit.jupiter.api.Order(1)
   @Test
//   @Disabled("flaky, time-based")
   void isFrequentBuyer() {
//      when(timeProvider.today()).thenReturn(parse("2021-09-08"));
//      target.doarPtTeste = () -> parse("2021-09-08");
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13, parse("2021-09-01"), parse("2021-09-08"))).thenReturn(List.of(new Order().setTotalAmount(130d)));

//      boolean result = target.isFrequentBuyer(13);
      LocalDate testTime = parse("2021-09-08");

//      try (
          MockedStatic<LocalDate> mockStatic = mockStatic(LocalDate.class);
//      ) {
         mockStatic.when(LocalDate::now).thenReturn(testTime);
         boolean result = target.isFrequentBuyer(13, null);

         System.out.println("in try{} " + now());
         assertThat(result).isTrue();
//      }
      System.out.println("Dupa: " + now());
   }
   @org.junit.jupiter.api.Order(2)
   @Test
   void xxxxaltTest() {

      System.out.println("Dupa: " + now());


      // 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
      // 2: interface for Clock retrival [general solution] -> **indirection without abstraction**
      // 3: inject a Supplier<LocalDate>
      // 4: pass time as method arg
      // 5: package-protected variant for testing
      // 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)
   }
}
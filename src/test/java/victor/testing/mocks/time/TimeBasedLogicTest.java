package victor.testing.mocks.time;

import org.junit.jupiter.api.Disabled;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeBasedLogicTest {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeBasedLogic target;
   private List<Order> orders = List.of(new Order().setTotalAmount(130d));

   @Test // 95 din cazuri esti inginer
   void isFrequentBuyer_inSiktir_imprecis_darVerdeSiMaine() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(eq(13), any(), any()))
              .thenReturn(orders);

      assertThat(target.isFrequentBuyer(13)).isTrue();
   }

   @Test
//   @Disabled("flaky, time-based")
   void isFrequentBuyer_mockuriStaticeCuMockitoInline_ultimaVer() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13, parse("2021-12-18"), parse("2021-12-25")))
              .thenReturn(orders);

      LocalDate fixedTime = parse("2021-12-25");

      try (MockedStatic<LocalDate> staticMock = mockStatic(LocalDate.class)) { // dep de pom: org.mockito:mockito-inline
         staticMock.when(() -> LocalDate.now()).thenReturn(fixedTime);
         boolean result = target.isFrequentBuyer(13);

         assertThat(result).isTrue();
      } // de aici incolo orice met statica din LocalDate ruleaza ca inainte curata


      // avertisment: 20 de ani mockurile statice au fost proscrise de comunitate. arse pe rug.
      // oameni pe SO urlau vai ce PROST esti daca mockuiesti met statice.
      // ca nu faci OOP mama ta de bizon.
      // de fapt batranii erau oripilati de mizeria produsa de librariile care ofereau mockuri statice (PowerMock)






      // 1: inject a Clock; Hint: you'll need ZoneId.systemDefault()
      // 2: interface for Clock retrival [general solution] -> **indirection without abstraction**
      // 3: inject a Supplier<LocalDate>
      // 4: pass time as method arg
      // 5: package-protected variant for testing
      // 6: mock now() - mocks all methods (try to add another LD.parse in the prod code) >> mockStatic(, CALLS_REAL_METHODS)
   }

   //   @Disabled("flaky, time-based")
   @Test
   void isFrequentBuyer() {
      LocalDate now = parse("2022-06-15");
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
              13, parse("2022-06-08"), now))
              .thenReturn(orders);

      boolean result = target.isFrequentBuyer_candTestuPute_designulProduluiPoateFiImbunatatit(13, now);

      assertThat(result).isTrue();
   }
}
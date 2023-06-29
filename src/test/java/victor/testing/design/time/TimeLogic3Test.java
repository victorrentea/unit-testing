package victor.testing.design.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeLogic3Test {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeLogic3 target;

   // subcutaneous test: extract the MEAT "logic" in a package-protected fucntion
   // and test that directly, by avoidung the thin "Skin" of the public method holding dependencies
   @Test
   void isFrequentBuyer() {
      LocalDate testTime = parse("2021-09-08");
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13,
          parse("2021-09-01"),
          testTime))
       .thenReturn(List.of(new Order().setTotalAmount(130d)));

      assertThat(target.isFrequentBuyerInternal(13,testTime)).isTrue();
   }
}

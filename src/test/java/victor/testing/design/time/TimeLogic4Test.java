package victor.testing.design.time;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeLogic4Test {
   OrderRepo orderRepoMock = mock(OrderRepo.class);
   TimeLogic4 target = new TimeLogic4(orderRepoMock);

   public static final String TEST_DATE = "2021-09-08";

   @Test
   @Disabled("flaky, time-based")
   void isFrequentBuyer() {
      when(orderRepoMock.findByCustomerIdAndCreatedOnBetween(
          13,
          parse("2021-09-01"),
          parse(TEST_DATE))).thenReturn(List.of(new Order().setTotalAmount(130d)));

      assertThat(target.isFrequentBuyer(13)).isTrue();
   }
}

// 1: inject a fixed Clock using TimeUtils
// 2: pass current date as an argument to a package-protected method (subcutaneous test)
// 3: mock statics LocalDate.now()
// 4: interface TimeProvider { LocalDate today(); } as a smell: **indirection without abstraction**
// 5: inject a Supplier<LocalDate>
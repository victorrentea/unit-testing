package victor.testing.design.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeLogic2Test {
   @Mock
   OrderRepo orderRepo;
   @InjectMocks
   TimeLogic2 target;


   @BeforeEach
   final void before() {
      LocalDate currentTime = parse("2021-09-08");
      // get a Clock instance fixed at this data
      // TODO exercise for hte readed
      Clock fixedClock = Clock.fixed(Instant.from(currentTime), ZoneId.systemDefault());
      target = new TimeLogic2(orderRepo, fixedClock);
   }

   @Test
   void isFrequentBuyer() {
      when(orderRepo.findByCustomerIdAndCreatedOnBetween(
          13,
          parse("2021-09-01"),
          parse("2021-09-08")))
       .thenReturn(List.of(new Order().setTotalAmount(130d)));

      assertThat(target.isFrequentBuyer(13)).isTrue();
   }
}

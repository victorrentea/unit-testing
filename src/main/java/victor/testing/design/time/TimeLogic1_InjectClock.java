package victor.testing.design.time;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeLogic1_InjectClock {
   private final OrderRepo orderRepo;
   private final Clock clock;
   // + in config: @Bean Clock clock() { return Clock.systemDefaultZone(); }

   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = LocalDate.now(clock);
//      LocalDate now = LocalDate.now(ClockHolder.clock); // variation
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

// variation: static clock
class ClockHolder {
   // fixed by default clock on 1 jan 2025 - all tests will assume that
   public static Clock clock = Clock.fixed(
       LocalDate.of(2025, 1, 1)
           .atStartOfDay(ZoneId.systemDefault()).toInstant(),
       ZoneOffset.UTC);
}

@SpringBootApplication
class SpringApp {
   public static void main (String[]args){
      // reset clock to normal on at normal startup (this single entry point)
      ClockHolder.clock = Clock.systemDefaultZone();
      SpringApplication.run(SpringApp.class, args);
   }
}

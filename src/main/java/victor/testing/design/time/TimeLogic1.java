package victor.testing.design.time;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
public class TimeLogic1 {
   private final OrderRepo orderRepo;
   private final Clock clock;

   public TimeLogic1(OrderRepo orderRepo, Clock clock) {
      this.orderRepo = orderRepo;
      this.clock = clock;
   }

   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = LocalDate.now(clock);
      LocalDate sevenDaysAgo = now.minusDays(7); // aritmetica/ calcule cu timpul

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

@Configuration
class UnConfig {
   @Bean
   public Clock clock() {
      return Clock.systemDefaultZone();
   }
}
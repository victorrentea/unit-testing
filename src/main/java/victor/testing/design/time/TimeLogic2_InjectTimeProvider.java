package victor.testing.design.time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
class TimeProvider {
   public LocalDate today() {
      return LocalDate.now();
   }
   public LocalDateTime now() {
      return LocalDateTime.now();
   }
   // ...
}
@Service
@RequiredArgsConstructor
public class TimeLogic2_InjectTimeProvider {
   private final OrderRepo orderRepo;
   private final TimeProvider timeProvider;

   // + in config: @Bean TimeProvider timeProvider() { return new DefaultTimeProvider(); }

   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = timeProvider.today();
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

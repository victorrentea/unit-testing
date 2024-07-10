package victor.testing.design.time;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeLogic2 {
   private final OrderRepo orderRepo;
   private final TimeProvider timeProvider;

   public TimeLogic2(OrderRepo orderRepo, TimeProvider timeProvider) {
      this.orderRepo = orderRepo;
     this.timeProvider = timeProvider;
   }

   // use this injection when you have time-based logic in many places.

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

class TimeProvider {
   public LocalDate today() {
      return LocalDate.now();
   }
}
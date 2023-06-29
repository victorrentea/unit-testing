package victor.testing.design.time;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeLogic4 {
   private final OrderRepo orderRepo;

   public TimeLogic4(OrderRepo orderRepo) {
      this.orderRepo = orderRepo;
   }

   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = LocalDate.now();
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

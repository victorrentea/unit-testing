package victor.testing.design.time;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeLogic3 {
   private final OrderRepo orderRepo;

   public TimeLogic3(OrderRepo orderRepo) {
      this.orderRepo = orderRepo;
   }

   // better than #1 because the class' public api DID NOT CHANGE
   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = LocalDate.now();
      return isFrequentBuyer(customerId, now);
   }

   @VisibleForTesting
   boolean isFrequentBuyer(int customerId, LocalDate now) {
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

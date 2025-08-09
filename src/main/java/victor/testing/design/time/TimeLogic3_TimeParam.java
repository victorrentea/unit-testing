package victor.testing.design.time;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeLogic3_TimeParam {
   private final OrderRepo orderRepo;

   public TimeLogic3_TimeParam(OrderRepo orderRepo) {
      this.orderRepo = orderRepo;
   }

   public boolean isFrequentBuyer(int customerId, LocalDate today) {
      LocalDate now = today;
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

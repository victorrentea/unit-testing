package victor.testing.time;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;

   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = LocalDate.now();
      LocalDate sevenDaysAgo = now.minusDays(7);

      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

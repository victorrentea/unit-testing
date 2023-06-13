package victor.testing.design.time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;

   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = LocalDate.now();
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(
          customerId,
          sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }

//   protected LocalDate hackMe() { //
//      return LocalDate.now();
//   }
}

//class TimeProvider {
//   public LocalDate today() {
//      return LocalDate.now();
//   }
//}
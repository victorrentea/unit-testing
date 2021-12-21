package victor.testing.mocks.time;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
class TimeProvider {
   public LocalDate currentDate() {
      return LocalDate.now();
   }
}

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;
   private final TimeProvider timeProvider;

   public boolean isFrequentBuyer(int customerId) {
      // untested !
      return isFrequentBuyer(customerId, LocalDate.now());
   }
   @VisibleForTesting
   boolean isFrequentBuyer(int customerId, LocalDate now) {
//      LocalDate now = timeProvider.currentDate();
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

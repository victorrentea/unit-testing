package victor.testing.design.time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.com.google.common.annotations.VisibleForTesting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;

   // how dare you pollute the public api under test JUST FOR THE SAKE OF TESTS
   public boolean isFrequentBuyer(int customerId) {
      return isFrequentBuyer(customerId, LocalDate.now());
   }
   @VisibleForTesting
    boolean isFrequentBuyer(int customerId, LocalDate now) {
//      LocalDate now = LocalDate.now(); // hidden coupling to the 'current' time
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(
              customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

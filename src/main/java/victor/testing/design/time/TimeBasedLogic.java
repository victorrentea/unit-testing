package victor.testing.design.time;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;

   public boolean isFrequentBuyer(int customerId) {
      return isFrequentBuyer_(customerId, LocalDate.now());
   }
   
   @VisibleForTesting
   boolean isFrequentBuyer_(int customerId, LocalDate now) {
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.
              findByCustomerIdAndCreatedOnBetween(customerId,
                      sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }

   // opt 1: subcutaneous tests: expose the hidden dep as a parameter
   // opt 2: static mocks
}

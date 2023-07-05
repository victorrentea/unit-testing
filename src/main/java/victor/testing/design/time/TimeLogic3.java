package victor.testing.design.time;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TimeLogic3 {
   private final OrderRepo orderRepo;
   private final TimeProvider timeProvider;

   public TimeLogic3(OrderRepo orderRepo, TimeProvider timeProvider) {
      this.orderRepo = orderRepo;
      this.timeProvider = timeProvider;
   }
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
   // wrap un apel static sub o metoda de isntanta, mockuibila
   public LocalDate today() {
      return LocalDate.now();
   }

   public String randomUUID() {
      return UUID.randomUUID().toString();
   }
}

package victor.testing.design.time;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeLogic1 {
   private final OrderRepo orderRepo;
   private final DateProvider dateProvider;

   public TimeLogic1(OrderRepo orderRepo, DateProvider dateProvider) {
      this.orderRepo = orderRepo;
      this.dateProvider = dateProvider;
   }

   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = dateProvider.now();
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}
@Component
class DateProvider {
   public LocalDate now() {
      return LocalDate.now();
   }
}
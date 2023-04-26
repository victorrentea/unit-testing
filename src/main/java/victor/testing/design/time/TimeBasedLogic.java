package victor.testing.design.time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;
   private final Supplier<LocalDate> dateSupplier;

   public boolean isFrequentBuyer(int customerId) {
//      LocalDate now = LocalDate.now();
      LocalDate now = dateSupplier.get();
      LocalDate sevenDaysAgo = now.minusDays(7); // ARITMETICA PE TIMP

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(
              customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

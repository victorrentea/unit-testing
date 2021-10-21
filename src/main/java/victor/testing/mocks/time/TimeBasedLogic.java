package victor.testing.mocks.time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;
   private final TimeProvider timeProvider;

   Supplier<LocalDate> doarPtTeste = LocalDate::now;

   public Order celeMaiMulteMetode() {
      return new Order()
          .setTotalAmount(10d)
          .setCreatedOn(LocalDate.now());
   }


   public boolean isFrequentBuyer(int customerId) {
      return isFrequentBuyer(customerId, LocalDate.now());
   }
   boolean isFrequentBuyer(int customerId, LocalDate now) {
//      LocalDate now = timeProvider.today();
//      LocalDate now = doarPtTeste.get();
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

@Component
class TimeProvider {
   public LocalDate today() {
      return LocalDate.now();
   }
}
package victor.testing.mocks.time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;
   private final Supplier<LocalDate> timeService;

   // frequent customer = paid more than 100 EUR over the last 7 days or c.genius=true
   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = timeService.get();
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}
// thin wrapper class over a static method, just to be able to mock it
// BAD. altering the desing of production (+1 file, +1dep everywhere) just for testing !
//class TimeService {
//   public LocalDate today() {
//      return LocalDate.now();
//   }
//}
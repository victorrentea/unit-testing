package victor.testing.time;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;
//   private final ClockAdapter clock;

   public boolean isFrequentBuyer(int customerId) {
      return isFrequentBuyerAsOfTime(customerId, now());
   }
   boolean isFrequentBuyerAsOfTime(int customerId, LocalDate now) {
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(
          customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }

 
}
class Interesting {
   TimeBasedLogic me;
   public void client() {
      me.isFrequentBuyer(1);
   }
}

//@Component
//class ClockAdapter { // indirection without abstraction (simplification)
//   public LocalDate today() {
//      return now(); // ugly library
//   }
//}

/// NEVER MOCK THESE  when(aFullName.getFirstName()).thenReturn("John"); ::  instead instantiate them
// new FullName("John", "DOE")
@Value
class FullName {
   String firstName;
   String lastName;
}
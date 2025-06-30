package victor.testing.design.time;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

//class OMG extends TimeLogic2 {
//   @Override
//   boolean isFrequentBuyer(int customerId, LocalDate now) {
//      return super.isFrequentBuyer(customerId, now);
//   }
//}
@Service
public /*final*/ class TimeLogic2 {
   private final OrderRepo orderRepo;

   public TimeLogic2(OrderRepo orderRepo) {
      this.orderRepo = orderRepo;
   }

//   @Transactional
//   @Timed
//   @Secured()
   public boolean isFrequentBuyer(int customerId) {
      return isFrequentBuyer(customerId, LocalDate.now());
   }
   @VisibleForTesting // subcutaneous test
   // javac only shows this method to classes in the same package
   // Sonar enforces that only tests are allowed to use this
   boolean isFrequentBuyer(int customerId, LocalDate now) {
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

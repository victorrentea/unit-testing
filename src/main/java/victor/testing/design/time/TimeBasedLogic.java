package victor.testing.design.time;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
class Alta {
   private final TimeBasedLogic timeBasedLogic;

   public void met() {
      timeBasedLogic.isFrequentBuyer(1, LocalDate.now());
   }
}
@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;

   public boolean isFrequentBuyer(int customerId) {
      LocalDate now = LocalDate.now();
      return isFrequentBuyer(customerId, now);
   }

   // test subcutanat( adica direct in carnita de sub o pele subtire)
   // rupt incapsularea, expunand pentru teste o functie
   // ce nu mai are dependinta de timpul curent
   // RISK: o clasa din prod din acelasi pachet brusc poate vedea acesta metoda.
   @VisibleForTesting // acum orice apel din aceelasi pachet la asta va irita Sonarul
   boolean isFrequentBuyer(int customerId, LocalDate now) {
      LocalDate sevenDaysAgo = now.minusDays(7); // ARITMETICA PE TIMP

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(
              customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

package victor.testing.mocks.time;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
   private final OrderRepo orderRepo;

   // Optiunea 4: introduci o noua clasa numita TimeProvider a ta pe care apoi o @Mock @InjectMocks ca de obicei
//   @Autowired
//   private TimeProvider timeProvider;

   public boolean  isFrequentBuyer(int customerId) {
//      LocalDate now = timeProvider.getNow(); //apel al unei metode DE INSTANTA dintr-o clasa de-a mea pe care sunt liber sa o mockuiesc cum imi tuna
      LocalDate now = LocalDate.now(); // cuplare cu timpul curent
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }

   public boolean  isFrequentBuyer_candTestuPute_designulProduluiPoateFiImbunatatit(int customerId, LocalDate now) {
//      LocalDate now = LocalDate.now(); // cuplare cu timpul curent
      LocalDate sevenDaysAgo = now.minusDays(7);

      System.out.println("Run with now=" + now);
      List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

      double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
      boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

      return totalAmount > 100 || anyGenius;
   }
}

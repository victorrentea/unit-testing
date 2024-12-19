package victor.testing.design.time;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

class TimeProvider {
  // fixed clock on 1 jan 2025 - all tests will assume that
  public static Clock clock = Clock.fixed(
      LocalDate.of(2025, 1, 1)
          .atStartOfDay(ZoneId.systemDefault()).toInstant(),
      ZoneOffset.UTC);
}

@SpringBootApplication
class SpringApp {
  public static void main (String[]args){
    // reset clock to normal on at normal startup (this single entry point)
    TimeProvider.clock = Clock.systemDefaultZone();
    SpringApplication.run(SpringApp.class, args);
  }
}

@Service
public class TimeLogic5 {
  private final OrderRepo orderRepo;

  public TimeLogic5(OrderRepo orderRepo) {
    this.orderRepo = orderRepo;
  }

  public boolean isFrequentBuyer(int customerId) {
    LocalDate now = LocalDate.now(TimeProvider.clock);
    LocalDate sevenDaysAgo = now.minusDays(7);

    System.out.println("Run with now=" + now);
    List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

    double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
    boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

    return totalAmount > 100 || anyGenius;
  }
}

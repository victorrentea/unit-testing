package victor.testing.design.time;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBasedLogic {
  private final OrderRepo orderRepo;
// #2 private final Supplier<LocalDateTime> localDateTimeSupplier;
  // design damage

  public boolean isFrequentBuyer(int customerId) {
    LocalDate now = LocalDate.now();
    return isFrequentBuyerInternal(customerId, now);
  }

  @VisibleForTesting // ❤️  #1 tells tools to block other prod calls to this
  boolean isFrequentBuyerInternal(int customerId, LocalDate now) {
    LocalDate sevenDaysAgo = now.minusDays(7);

    System.out.println("Run with now=" + now);
    List<Order> recentOrders = orderRepo.findByCustomerIdAndCreatedOnBetween(customerId, sevenDaysAgo, now);

    double totalAmount = recentOrders.stream().mapToDouble(Order::getTotalAmount).sum();
    boolean anyGenius = recentOrders.stream().anyMatch(Order::isGenius);

    return totalAmount > 100 || anyGenius;
  }
}

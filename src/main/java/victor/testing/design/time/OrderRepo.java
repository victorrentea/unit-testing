package victor.testing.design.time;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
   List<Order> findByCustomerIdAndCreatedOnBetween(int customerId, LocalDateTime from, LocalDateTime to);

   List<Order> findByActiveTrue();
}

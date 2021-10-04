package victor.testing.time;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {
   List<Order> findByCustomerIdAndCreatedOnBetween(int customerId, LocalDate from, LocalDate to);

   List<Order> findByActiveTrue();
}

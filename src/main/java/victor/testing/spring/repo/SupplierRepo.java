package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.testing.spring.domain.Supplier;

import java.util.Optional;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
   Optional<Supplier> findByName(String name);

   Optional<Supplier> findByCodeIgnoreCase(String code);
}

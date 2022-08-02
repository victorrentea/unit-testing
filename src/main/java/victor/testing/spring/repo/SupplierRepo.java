package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.testing.spring.domain.Supplier;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
   Supplier findByName(String supplierName);
}

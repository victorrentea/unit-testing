package victor.testing.spring.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.testing.spring.product.domain.Supplier;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
   Supplier findByName(String supplierName);
}

package ro.victor.unittest.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.victor.unittest.spring.domain.Supplier;

import java.util.Optional;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
}

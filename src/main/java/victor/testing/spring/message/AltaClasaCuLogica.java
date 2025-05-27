package victor.testing.spring.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

@Slf4j
@RequiredArgsConstructor
@Service
public class AltaClasaCuLogica {
  private final SupplierRepo supplierRepo;
  public void process(String supplierName) {
    if (!supplierRepo.existsByName(supplierName))
        supplierRepo.save(new Supplier().setName(supplierName));
  }
}

package victor.testing.spring.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class BizLogic {
  private final SupplierRepo supplierRepo;
  public void bizLogic(String supplierName) {
    afterAWhile();
    var supplier = new Supplier()
        .setName(requireNonNull(supplierName));
    supplierRepo.save(supplier);
    log.info("Created supplier");
  }

  private void afterAWhile() {
    try {
      Thread.sleep(35);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}

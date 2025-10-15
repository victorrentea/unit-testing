package victor.testing.spring.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

@Service
public class AsyncService {
  private final SupplierRepo supplierRepo;

  public AsyncService(SupplierRepo supplierRepo) {
    this.supplierRepo = supplierRepo;
  }

  public CompletableFuture<Long> asyncReturning(String supplierName) {
    return CompletableFuture.supplyAsync(() -> {
      afterAWhile();
      var supplier = new Supplier()
          .setName(requireNonNull(supplierName));
      return supplierRepo.save(supplier).getId();
    });
  }

  public void fireAndForget(String supplierName) {
    CompletableFuture.runAsync(() -> {
      afterAWhile();
      var supplier = new Supplier()
          .setName(requireNonNull(supplierName));
      supplierRepo.save(supplier);
    });
  }

  @Async
  public void fireAndForgetSpring(String supplierName) {
    afterAWhile();
    var supplier = new Supplier()
        .setName(requireNonNull(supplierName));
    supplierRepo.save(supplier);
  }

  private void afterAWhile() {
    try {
      Thread.sleep((long) (30 + 30 * Math.random()));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}

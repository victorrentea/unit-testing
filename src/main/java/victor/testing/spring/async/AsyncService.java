package victor.testing.spring.async;

import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
  private final SupplierRepo supplierRepo;

  public AsyncService(SupplierRepo supplierRepo) {
    this.supplierRepo = supplierRepo;
  }

  // @Async // or via Spring Magic
  public CompletableFuture<String> asyncReturning(String supplierName) {
    return CompletableFuture.supplyAsync(() -> {
      takesAWhile();
      supplierRepo.save(new Supplier().setName(supplierName));
      return "stuff retrieved in parallel";
    });
  }

  // @Async // or via Spring Magic
  public void asyncFireAndForget(String supplierName) throws InterruptedException {
    CompletableFuture.runAsync(() -> {
      takesAWhile();
      supplierRepo.save(new Supplier().setName(supplierName));
      // experiment: an error
    });
  }

  private void takesAWhile() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}

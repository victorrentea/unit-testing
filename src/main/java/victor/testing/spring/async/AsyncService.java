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
  public CompletableFuture<Supplier> asyncReturning(String supplierName) {
    return CompletableFuture.supplyAsync(() -> {
      takesAWhile();
      return supplierRepo.findByName(supplierName).orElseThrow();
    });
  }

  // @Async // or via Spring Magic
  public void asyncFireAndForget(String supplierName) throws InterruptedException {
    CompletableFuture.runAsync(() -> {
      takesAWhile();
      // if (true) throw new RuntimeException("experiment"); // TODO
      supplierRepo.save(new Supplier().setName(supplierName));
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

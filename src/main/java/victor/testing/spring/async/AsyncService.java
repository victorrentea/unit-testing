package victor.testing.spring.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class AsyncService {
  private final SupplierRepo supplierRepo;

  public AsyncService(SupplierRepo supplierRepo) {
    this.supplierRepo = supplierRepo;
  }

  @Async // or via Spring Magic
  public CompletableFuture<String> asyncReturning(String supplierName) {
    takesAWhile();
    supplierRepo.save(new Supplier().setName(supplierName));
    return completedFuture("stuff retrieved in parallel");
  }
  //  @Test { var cf=asyncReturning(..); cf.get


  @Async // or via Spring Magic
  public void asyncFireAndForget(String supplierName) throws InterruptedException {
//    CompletableFuture.runAsync(() -> {
    takesAWhile();
    supplierRepo.save(new Supplier()
        .setName("xxx"));
    // experiment: an error
//    });
  }
  // @Test {asyncFireAndForget(..,);


  private void takesAWhile() {
    try {
//      if (true) throw new RuntimeException("Intentional");
      Thread.sleep(100);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}

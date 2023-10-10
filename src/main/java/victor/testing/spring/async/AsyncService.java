package victor.testing.spring.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class AsyncService {
  @Autowired
  private SupplierRepo supplierRepo;

  @Async
  public CompletableFuture<String> asyncReturning() throws InterruptedException {
    Thread.sleep(100);
    return completedFuture("stuff");
  }
  @Async
  public void asyncFireAndForget(String supplierName) throws InterruptedException {
    Thread.sleep(100);
    supplierRepo.save(new Supplier().setName(supplierName));
  }

}

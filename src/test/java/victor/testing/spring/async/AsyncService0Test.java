package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

class AsyncService0Test extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void asyncReturning() throws Exception {
    CompletableFuture<Long> futureId = asyncService.asyncReturning("sname");

//    CompletableFuture<String> futureString = futureId.thenCompose(id -> f(id));

    Long id = futureId.get(); //block the test
    Supplier supplierInDB = supplierRepo.findById(id).orElseThrow();
    assertThat(supplierInDB.getName()).isEqualTo("sname");
    // TODO assert the supplier is inserted correctly
  }

  private CompletableFuture<String> f(Long id) {
    return CompletableFuture.completedFuture("" + id); // imagine a rest call here
  }
  // TODO +1 @Test: no supplier is inserted if name is null

  @Test
  void fireAndForget() throws Exception {
    asyncService.fireAndForget("sname");

    // TODO assert the supplier is inserted correctly
    //  Tip: use Awaitility to poll every 10ms for max 5 seconds
  }
  // TODO +1 @Test: no supplier is inserted if name is null

  @Test
  void fireAndForgetSpring() throws Exception {
    asyncService.fireAndForgetSpring("sname");

    // TODO assert the supplier is inserted correctly
    //  Tip: you can disable the @Async annotation via a property: see AsyncConfig
  }
}

package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AsyncService0Test extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void asyncReturning() throws Exception {
    CompletableFuture<Long> futureId = asyncService.asyncReturning("sname");

    // Mono/Flux/Future/Single.block()
    Long id = futureId.get(); //block the test
    Supplier supplierInDB = supplierRepo.findById(id).orElseThrow();
    assertThat(supplierInDB.getName()).isEqualTo("sname");
  }

  @Test
  void asyncTHROWS() throws Exception {
    CompletableFuture<Long> futureId = asyncService.asyncReturning(null);

    assertThatThrownBy(() -> {
      futureId.get(); //throws
    } ).hasCauseInstanceOf(NullPointerException.class);
    assertThat(supplierRepo.findAll()).isEmpty(); // ±
  }











  @Test
  void fireAndForget() throws Exception {
    asyncService.fireAndForget("sname");

    Thread.sleep(100);
    assertThat(supplierRepo.findByName("sname")).isPresent();

//    Awaitility.await()
//        .until
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

package victor.testing.spring.async;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@Transactional // doesnt work with multithreading; prod code in thread#2 did commit despite @Test tx ROLLBACK
@TestPropertySource(properties = "async.enabled=false")
class AsyncService0Test extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @BeforeEach
  final void before() {
    supplierRepo.deleteAll();
  }

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

//    Thread.sleep(500); // delay both OK and KO

    Awaitility.await() // reduce the time to wait for the happy case
        .pollDelay(ofMillis(0))
        .pollInterval(ofMillis(5))
        .untilAsserted(()->
          assertThat(supplierRepo.findByName("sname")).isPresent()
            );
    // TODO assert the supplier is inserted correctly
    //  Tip: use Awaitility to poll every 10ms for max 5 seconds
  }
  // TODO +1 @Test: no supplier is inserted if name is null


  @Test
  void fireAndForgetKO() throws Exception {
    asyncService.fireAndForget(null);

    // check the production code did NOT do a side effect
    // DO NOT use AwAITILITY
    Thread.sleep(1000); // 🤢LARGE enough for the weakest machine running this
    // large = constant stealing life from colleagues.
    assertThat(supplierRepo.findAll()).isEmpty();

    // 🤢no way to assert the exception thrown in another thread
  }
















  @Test
  void fireAndForgetSpring() throws Exception {
    asyncService.fireAndForgetSpring("sname");

    assertThat(supplierRepo.findByName("sname")).isPresent();
  }

  @Test
  void fireAndForgetSpringKO() throws Exception {
    assertThatThrownBy(() ->
          asyncService.fireAndForgetSpring(null))
        .isInstanceOf(NullPointerException.class);
    assertThat(supplierRepo.findAll()).isEmpty();
  }
}

package victor.testing.spring.async;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AsyncService0Test extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void asyncReturning() throws Exception {
    CompletableFuture<Long> futureId = asyncService.asyncReturning("sname");
    Long id = futureId.get();
    assertThat(supplierRepo.findById(id)).isPresent();
  }
  @Test
  void asyncReturning_doesNotInsert() throws Exception {
    CompletableFuture<Long> futureId = asyncService.asyncReturning(null);
    assertThatThrownBy(() -> futureId.get()); // bloche
    assertThat(supplierRepo.findAll()).isEmpty();
  }

  @Test
  void fireAndForget() throws Exception {
    asyncService.fireAndForget("sname");

    Awaitility.await()
        .pollDelay(ofMillis(10))
        .atMost(ofSeconds(5))
        .untilAsserted(()->assertThat(supplierRepo.findByName("sname")).isPresent());
  }
  @Test
  void fireAndForget_nuInseraDacaNameNull() throws Exception {
    asyncService.fireAndForget(null);

    Thread.sleep(3000); // greu de evitat
    assertThat(supplierRepo.findAll()).isEmpty();
  }

  @Test
  void fireAndForgetSpring() throws Exception {
    asyncService.fireAndForgetSpring("sname");

    // TODO assert the supplier is inserted correctly
    //  Tip: you can disable the @Async annotation via a property: see AsyncConfig
  }
}

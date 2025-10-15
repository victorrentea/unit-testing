package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    Thread.sleep(80); // M1 MAX 64G
    assertThat(supplierRepo.findByName("sname")).isPresent();
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

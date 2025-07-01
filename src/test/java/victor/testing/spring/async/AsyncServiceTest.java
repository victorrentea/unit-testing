package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

public class AsyncServiceTest extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void asyncReturning() throws Exception {
    asyncService.asyncReturning("sname");

    // TODO assert the supplier is inserted correctly
  }
  // TODO add another test to check a null name is rejected

  @Test
  void asyncFireAndForget() throws Exception {
    asyncService.asyncFireAndForget("sname");

    // TODO assert the supplier is inserted correctly
    //  Tip: use Awaitility. change pollInterval
  }
  // TODO add another test to check a null name is rejected

  @Test
  void asyncFireAndForgetSpring() throws Exception {
    asyncService.asyncFireAndForgetSpring("sname");

    // TODO assert the supplier is inserted correctly
    //  Tip: disable the @Async annotation: see AsyncConfig
  }
}

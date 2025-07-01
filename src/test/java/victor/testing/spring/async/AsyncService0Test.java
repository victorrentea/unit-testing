package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

public class AsyncService0Test extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void asyncReturning() throws Exception {
    asyncService.asyncReturning("sname");

    // TODO assert the supplier is inserted correctly
  }
  // TODO +1 @Test: null name is not saved

  @Test
  void asyncFireAndForget() throws Exception {
    asyncService.asyncFireAndForget("sname");

    // TODO assert the supplier is inserted correctly
    //  Tip: use Awaitility. change pollInterval
  }
  // TODO +1 @Test: null name is not saved

  @Test
  void fireAndForgetSpring() throws Exception {
    asyncService.fireAndForgetSpring("sname");

    // TODO assert the supplier is inserted correctly
    //  Tip: you can disable the @Async annotation via a property: see AsyncConfig
  }
}

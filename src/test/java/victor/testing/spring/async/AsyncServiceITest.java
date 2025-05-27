package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;


//@TestPropertySource(properties = "async.enabled=false") // disable @Async:
public class AsyncServiceITest extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  // TODO discuss: UUID ~> Independent data slices (parallel tests)
  // TODO discuss: (a) insert reference via global SQL vs (b) insert your own reference data
  String SUPPLIER_NAME = "sname" + UUID.randomUUID();

  @Test
  void block_for_result() throws InterruptedException, ExecutionException {
    var supplierId = supplierRepo.save(new Supplier().setName(SUPPLIER_NAME)).getId();
    Supplier result = asyncService.asyncReturning(SUPPLIER_NAME).get(); // block JUnit thread until completed

    assertThat(supplierRepo.findByName(SUPPLIER_NAME)).isPresent();
    assertThat(result.getId()).isEqualTo(supplierId);
  }

  @Test
  void poll_for_effect() throws InterruptedException {
    asyncService.asyncFireAndForget(SUPPLIER_NAME);

    Example<Supplier> example = Example.of(new Supplier().setName(SUPPLIER_NAME));
    Awaitility.await()
        .timeout(ofSeconds(1))
        .untilAsserted(() -> assertThat(supplierRepo.findAll(example)).hasSize(1));
  }
}

package victor.testing.spring.async;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

  @BeforeEach
  @AfterEach
  final void cleanup() { // different thread => different transaction
    supplierRepo.deleteAll();
  }

  @Test
  void asyncFetch_block() throws InterruptedException, ExecutionException {
    var id = asyncService.asyncReturning("sname").get(); // block JUnit thread until completed

    assertThat(supplierRepo.findById(id)).get()
        .returns("sname", Supplier::getName);
  }

  @Test
  void fireAndForget_poll() throws InterruptedException, ExecutionException {
    asyncService.asyncFireAndForget("sname");

    Awaitility.await()
        .timeout(ofSeconds(1))
        .untilAsserted(() -> {
          assertThat(supplierRepo.findAll()).hasSize(1)
              .first()
              .extracting(Supplier::getName)
              .isEqualTo("sname");
        });
  }
}

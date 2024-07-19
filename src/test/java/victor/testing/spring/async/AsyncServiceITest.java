package victor.testing.spring.async;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

// to disable @Async:
@TestPropertySource(properties = "async.enabled=true")
public class AsyncServiceITest extends IntegrationTest {
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
    CompletableFuture<String> cf = asyncService.asyncReturning("sname");
    String result = cf.get(); // block JUnit thread until completed

    assertThat(supplierRepo.findAll()).hasSize(1)
        .first()
        .returns("sname", Supplier::getName);
    assertThat(result).isEqualTo("stuff retrieved in parallel");
  }

  @Test
  void fireAndForget_poll() throws InterruptedException, ExecutionException {
    asyncService.asyncFireAndForget("sname");

    Supplier s = Awaitility.await()
        .timeout(ofSeconds(5))
        .until(() -> supplierRepo.findAll().stream().findFirst().orElse(null),
            Objects::nonNull);

    assertThat(s.getName()).isEqualTo("sname");
  }
}

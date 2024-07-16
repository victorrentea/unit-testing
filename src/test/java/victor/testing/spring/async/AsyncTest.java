package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
public class AsyncTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void asyncFetch_block() throws InterruptedException, ExecutionException {
    String result = asyncService.asyncFetch().get(); // block JUnit thread until completed

    assertThat(result).isEqualTo("stuff retrieved in parallel");
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

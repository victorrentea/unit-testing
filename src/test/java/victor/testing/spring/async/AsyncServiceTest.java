package victor.testing.spring.async;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

public class AsyncServiceTest extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @BeforeEach
    void before() {
        supplierRepo.deleteAll();
    }

    @Nested
    @DirtiesContext
    class JavaAsyncTest {
      @Test
      void asyncReturning() throws Exception {
        CompletableFuture<Long> sname = asyncService.asyncReturning("sname");

        Awaitility.await().timeout(ofSeconds(5))
                .untilAsserted(() -> assertThat(sname.isDone()).isTrue());
        Long id = sname.get();
        Supplier supplier = supplierRepo.findById(id).orElseThrow();
        assertThat(supplier.getName()).isEqualTo("sname");
      }

      @Test
      void asyncFireAndForget() throws Exception {
        asyncService.asyncFireAndForget("sname");

        Awaitility.await().timeout(ofSeconds(5))
                .untilAsserted(() -> {
                  assertThat(supplierRepo.findByName("sname")).isPresent();
                });
      }

      @Test
      void asyncFireAndForgetWithNull() throws Exception {
        asyncService.asyncFireAndForget(null);

        Awaitility.await().pollDelay(ofSeconds(5))
                .untilAsserted(() -> {
                  assertThat(supplierRepo.count()).isEqualTo(0L);
                });
      }
    }

  @Nested
  @TestPropertySource(
          locations = "classpath:application-test.properties",
          properties = "async.enabled=true")
  @DirtiesContext
  public class SpringAsyncTest {
    @Test
    public void asyncFireAndForgetSpring() throws Exception {
      asyncService.asyncFireAndForgetSpring("sname");

      Awaitility.await().timeout(ofSeconds(5))
              .untilAsserted(() -> {
                assertThat(supplierRepo.findByName("sname")).isPresent();
              });
    }
  }

  @AfterEach
  void after() {
    supplierRepo.deleteAll();
  }
}

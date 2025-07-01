package victor.testing.spring.async;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AsyncServiceTest extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @BeforeEach
  void deleteAll(){
    supplierRepo.deleteAll();
  }

  @Test
  void asyncReturning() throws Exception {
    Long id = asyncService.asyncReturning("sname").get();
    assertThat(supplierRepo.findById(id).get().getName()).isEqualTo("sname");
  }

  @Test
  void asyncReturning_Exception_null() {
    assertThatThrownBy(() -> asyncService.asyncReturning(null).get())
            .hasRootCauseInstanceOf(NullPointerException.class);
  }

  @Test
  void asyncFireAndForget_SupplierName() throws Exception {
    asyncService.asyncFireAndForget("sname");

    Awaitility.await().atMost(ofSeconds(2)).untilAsserted(()
            -> assertThat(supplierRepo.findByName("sname")).isPresent());
  }

  @Test
  void asyncFireAndForget_SupplierNullName() throws Exception {
    asyncService.asyncFireAndForget(null);
    Thread.sleep(2000);
    assertThat(supplierRepo.count()).isEqualTo(0);
  }

  @Test
  void asyncFireAndForgetSpring() throws Exception {
    asyncService.asyncFireAndForgetSpring("sname");

    // TODO assert the supplier is inserted correctly
    //  Tip: disable the @Async annotation: see AsyncConfig
  }
}

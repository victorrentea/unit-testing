package victor.testing.spring.async;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AsyncServiceTest extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @BeforeEach
  final void before() {
    supplierRepo.deleteAll();
  }

  @Test
  void asyncReturning() throws Exception {
    var id = asyncService.asyncReturning("sname").get(); // block junit thread

    var supplier = supplierRepo.findById(id).orElseThrow();
    assertThat(supplier.getName()).isEqualTo("sname");
  }

  @Test
  void asyncReturning_throwsForNullName() throws Exception {
    assertThatThrownBy(() -> asyncService.asyncReturning(null).get());
    assertThat(supplierRepo.findAll()).isEmpty();
  }

  @Test
  void fireAndForget() throws Exception {
    asyncService.fireAndForget("sname");

    Awaitility.await().timeout(Duration.ofSeconds(2)).untilAsserted(() ->
        assertThat(supplierRepo.findByName("sname")).isPresent());
  }

  @RepeatedTest(5) // flaky test
  void fireAndForgetSURPRISE() throws Exception {
    asyncService.fireAndForget("sname");

    Awaitility.await().timeout(Duration.ofSeconds(2)).untilAsserted(() ->
        supplierRepo.findByName("sname").get());
  }

  @Test
  void fireAndForget_failsForNullName() throws Exception {
    asyncService.fireAndForget(null);

    Thread.sleep(2000);
    // if the side-effect didn't happen yet, it will NEVER happen
    assertThat(supplierRepo.findAll()).isEmpty();
  }

  @Test
  void fireAndForgetSpring() throws Exception {
    asyncService.fireAndForgetSpring("sname");// runs sync as @Async  was "disabled"
    assertThat(supplierRepo.findByName("sname")).isPresent();
  }

  @Test
  void fireAndForgetSpring_nullName() throws Exception {
    assertThatThrownBy(() -> asyncService.fireAndForgetSpring(null));
    assertThat(supplierRepo.findAll()).isEmpty();
  }
}
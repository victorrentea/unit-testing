package victor.testing.spring.async;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import java.time.Duration;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

//@TestPropertySource(properties = "async.enabled=false")
public class AsyncServiceTest extends IntegrationTest {
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
  void asyncFireAndForget() throws Exception {
    asyncService.asyncFireAndForget("sname");

    await().timeout(ofSeconds(2)).untilAsserted(() ->
        assertThat(supplierRepo.findByName("sname")).isPresent());
  }
//  @Test
  @RepeatedTest(50) // proves it's a flaky test
  void asyncFireAndForgetSURPRISE() throws Exception {
    asyncService.asyncFireAndForget("sname");

    await().timeout(ofSeconds(2)).untilAsserted(() ->
        supplierRepo.findByName("sname").get());
  }
  @Test
  void asyncFireAndForget_failsForNullName() throws Exception {
    asyncService.asyncFireAndForget(null);

    // #bad (over
//    await().timeout(ofSeconds(2)).untilAsserted(() ->
//        assertThat(supplierRepo.findAll()).isEmpty());

    // #good:
    Thread.sleep(2000); // duration within which
    // if the side effect didn't happen yet, it will NEVER happen
    assertThat(supplierRepo.findAll()).isEmpty();

    // #bad: polls for no reason.
    // after 5 seconds, I don't need to RETRY the assert. only run it once
//    Awaitility.await()
//        .pollDelay(ofSeconds(5))
//        .untilAsserted(() -> {
//          assertThat(supplierRepo.count()).isEqualTo(0L);
//        });
  }
  // TODO add another test to check a null name is rejected

  @Test
  void asyncFireAndForgetSpring() throws Exception {
    asyncService.asyncFireAndForgetSpring("sname");// runs sync as @Async  was "disabled"
    assertThat(supplierRepo.findByName("sname")).isPresent();
  }
  @Test
  void asyncFireAndForgetSpringNullName() throws Exception {
    assertThatThrownBy(() -> asyncService.asyncFireAndForgetSpring(null));
    assertThat(supplierRepo.findAll()).isEmpty();
  }
}

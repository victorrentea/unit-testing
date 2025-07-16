package victor.testing.spring.async;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

// #RAU strica spring test context cache = +20..30 la build time
// cand apar in subclase ale lui IntegrationTest
//@MockitoSpyBean
//@MockitoBean
//@ActiveProfile
//@TestPropertySource(properties = "prop=alta")
//@TestConfiguration
// MOrala: le pui pe astea doar in superclasa comuna, aici : IntegrationTest
// pt a micsora nr de conf distincte de spring de test => mai putine 'bannere' => mai rapide teste

//@Transactional // nu ajuta pt ca INSERTul se face intr-o alta tx decat cea din test
public class AsyncService0Test extends IntegrationTest {
  @Autowired
  AsyncService asyncService;
  @BeforeEach
  @AfterEach
  void cleanupDB() {
    supplierRepo.deleteAll();
  }

  @Test
  void asyncReturning() throws Exception {
    CompletableFuture<Long> future = asyncService.asyncReturning("sname");

    Long id = future.get();

    Supplier supplier = supplierRepo.findById(id).orElseThrow();
    assertThat(supplier.getName()).isEqualTo("sname");
  }

  @Test
  void asyncThrowsForNullName() throws Exception {
    CompletableFuture<Long> future = asyncService.asyncReturning(null);

    assertThatThrownBy(() -> future.get());
  }

  @Test
  void fireAndForgetSpring_hate() throws Exception {
    asyncService.fireAndForgetSpring("sname");

    Thread.sleep(/*3*60**/300); // flaky #Doamne fereste!

    assertThat(supplierRepo.findByName("sname")).isPresent();

  }

  @Test
  void fireAndForgetSpring_polling() throws Exception {
    asyncService.fireAndForgetSpring("sname");
    //💖
    // mai usor de inteles
    // mai aproape de realitate
    await()
        .timeout(ofMinutes(3))
        .untilAsserted(() ->
            assertThat(supplierRepo.findByName("sname"))
                .isPresent());

  }
}

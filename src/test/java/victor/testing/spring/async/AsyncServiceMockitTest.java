package victor.testing.spring.async;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
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
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ActiveProfiles({"test","doi"})
@Transactional // nu ajuta pt ca INSERTul se face intr-o alta tx decat cea din test
public class AsyncServiceMockitTest extends IntegrationTest {
  @Autowired
  AsyncService asyncService;

  @Captor
  ArgumentCaptor<Supplier> supplierCaptor;

  @Test
  void fireAndForgetSpring_blocking_verify() throws Exception {
    asyncService.fireAndForgetSpring("sname");

    verify(supplierRepo, timeout(1000))
        .save(supplierCaptor.capture());
    Supplier supplier = supplierCaptor.getValue();
    assertThat(supplier.getName()).isEqualTo("sname");
  }
}

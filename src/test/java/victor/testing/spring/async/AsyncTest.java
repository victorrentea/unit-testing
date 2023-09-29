package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.repo.SupplierRepo;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
public class AsyncTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void asyncReturning() throws InterruptedException, ExecutionException {
    String result = asyncService.asyncReturning().get();
    assertThat(result).isEqualTo("stuff");
  }

  @Test
  void asyncFireAndForget_disablingAsync() throws InterruptedException {
    asyncService.asyncFireAndForget("sname");
    assertThat(supplierRepo.findAll()).hasSize(1)
        .first()
        .extracting(Supplier::getName)
        .isEqualTo("sname");
  }
  @TestConfiguration
  // Disable @Async for this test
  @EnableAsync(annotation = DisableAsync.NotUsed.class)
  public static class DisableAsync {
    @interface NotUsed {}
  }
}

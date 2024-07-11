package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
public class DisablingAsyncTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

  @TestConfiguration // this hack disables the @Async behavior
  @EnableAsync(annotation = Test.class) // @Test never appears in production code
  public static class DisableAsync {
  }

  @Test
  void asyncFireAndForget_disablingAsync() throws InterruptedException {
    asyncService.asyncFireAndForget("sname");
    assertThat(supplierRepo.findAll()).hasSize(1)
        .first()
        .extracting(Supplier::getName)
        .isEqualTo("sname");
  }
}

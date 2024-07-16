package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"db-mem"})
//✅Option#1 via manual property
//@TestPropertySource(properties = "async.enabled=false")
public class DisablingAsyncTest {
  @Autowired
  AsyncService asyncService;
  @Autowired
  SupplierRepo supplierRepo;

//  @TestConfiguration // ✅Option#2 disables the @Async annotation
  @EnableAsync(annotation = Test.class) // @Test never appears in production code
  public static class DisableAsync {
  }

  @TestConfiguration // ✅💖Option#3: replace the default ThreadPoolTaskExecutor with a synchronous one
  // 💖💖💖 cleaner, without any prod impact
  public static class SyncExecutor {
    @Bean
    public TaskExecutor taskExecutor() {
      return new SyncTaskExecutor();
    }
  }

  @Test
  void asyncFireAndForget() throws InterruptedException {
    asyncService.asyncFireAndForget("sname"); // runs in my thread

    assertThat(supplierRepo.findAll()).hasSize(1)
        .first()
        .extracting(Supplier::getName)
        .isEqualTo("sname");
  }
}

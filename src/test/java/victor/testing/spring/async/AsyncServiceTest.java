package victor.testing.spring.async;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AsyncServiceTest {
  @Mock
  SupplierRepo supplierRepo;
  @InjectMocks
  AsyncService asyncService;

  @Test
  void asyncReturning_block() throws InterruptedException, ExecutionException {
    CompletableFuture<String> future = asyncService.asyncReturning("sname");
    // block JUnit thread until completed
    String result = future.get();

    assertThat(result).isEqualTo("stuff retrieved in parallel");
    verify(supplierRepo)
        .save(argThat(s -> s.getName().equals("sname")));
  }

  @Test
  void fireAndForget_poll() throws InterruptedException, ExecutionException {
    asyncService.asyncFireAndForget("sname");

    verify(supplierRepo, timeout(1000))
        .save(argThat(s -> s.getName().equals("sname")));
  }
}

package victor.testing.spring.parallelTests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

// TODO To see the race, enable running @Tests on multiple threads via junit-platform.properties
//@Execution(ExecutionMode.SAME_THREAD) // FIX
class WhenRunningTestsInParallel_MockBeanCanRaceTest extends IntegrationTest {
  @MockitoBean
  SupplierRepo supplierRepo;

  @Test
  void finds() throws InterruptedException {
    when(supplierRepo.findById(1L)).thenReturn(Optional.of(new Supplier()));

    Thread.sleep(50); // imagine some delay in tested code
    assertThat(supplierRepo.findById(1L)).isNotEmpty();
  }
  @Test
  void doesntFind() throws InterruptedException {
    when(supplierRepo.findById(1L)).thenReturn(Optional.empty());

    Thread.sleep(50); // imagine some delay in tested code
    assertThat(supplierRepo.findById(1L)).isEmpty();
  }

}

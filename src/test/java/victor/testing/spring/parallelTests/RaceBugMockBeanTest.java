package victor.testing.spring.parallelTests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductCreatedEvent;
import victor.testing.spring.service.ProductService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

@SpringBootTest
@ActiveProfiles("db-mem")
@Disabled("Race happens when enabling parallel tests - see junit-platform.properties")
//@Execution(ExecutionMode.SAME_THREAD) // FIX: force all @Test in this class to run single thread when using parallel tests
public class RaceBugMockBeanTest {
  @Autowired
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

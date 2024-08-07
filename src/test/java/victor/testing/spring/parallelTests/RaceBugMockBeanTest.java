package victor.testing.spring.parallelTests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

// without the followin annotation, running tests in parallel causes flaky tests
//@Execution(ExecutionMode.SAME_THREAD) // force all @Test in this class to run single thread when using parallel tests
@SpringBootTest
@ActiveProfiles("db-mem")
@Disabled // demonstrated with parallel testing enabled
public class RaceBugMockBeanTest {
  @MockBean
  SafetyApiAdapter safetyApiAdapter;
  @MockBean
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

  @Test
  void throwsExForUnsafe() throws InterruptedException {
    when(safetyApiAdapter.isSafe("unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "unsafe", "S", HOME);

    Thread.sleep(50); // imagine some delay in tested code
    assertThatThrownBy(() -> productService.createProduct(dto));
  }

  @Test
  void ok() throws InterruptedException {
    supplierRepo.save(new Supplier().setCode("S")).getId();
    when(safetyApiAdapter.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safe", "S", HOME);

    Thread.sleep(50); // imagine some delay in tested code
    productService.createProduct(dto);
  }

}

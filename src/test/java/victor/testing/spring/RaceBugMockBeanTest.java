package victor.testing.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

// without the followin annotation, running tests in parallel causes flaky tests
//@Execution(ExecutionMode.SAME_THREAD) // force all @Test in this class to run single thread when using parallel tests
public class RaceBugMockBeanTest extends IntegrationTest{
  @MockBean
  SafetyApiClient safetyApiClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

  @Test
  void throwsExForUnsafe() throws InterruptedException {
    when(safetyApiClient.isSafe("unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "unsafe", "S", HOME);

    Thread.sleep(50); // imagine some delay in tested code
    assertThatThrownBy(() -> productService.createProduct(dto));
  }

  @Test
  void ok() throws InterruptedException {
    Long supplierId = supplierRepo.save(new Supplier().setCode("S")).getId();
    when(safetyApiClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safe", "S", HOME);

    Thread.sleep(50); // imagine some delay in tested code
    productService.createProduct(dto);
  }

}

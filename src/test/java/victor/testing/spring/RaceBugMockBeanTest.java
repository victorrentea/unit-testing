package victor.testing.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.create.ProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;

// without the followin annotation, running tests in parallel causes flaky tests
//@Execution(ExecutionMode.SAME_THREAD) // force all @Test in this class to run single thread when using parallel tests
public class RaceBugMockBeanTest extends IntegrationTest{
  @MockBean
  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

  @Test
  void throwsEx() throws InterruptedException {
    when(safetyClient.isSafe("unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "unsafe", -1L, HOME);

    Thread.sleep(50); // imagine some delay in tested code
    assertThatThrownBy(() -> productService.createProduct(dto));
  }

  @Test
  void ok() throws InterruptedException {
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(safetyClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safe", supplierId, HOME);

    Thread.sleep(50); // imagine some delay in tested code
    productService.createProduct(dto);
  }

}

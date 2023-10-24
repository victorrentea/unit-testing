package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;


public class CreateProductBisTest extends IntegrationTest {
  @Autowired // it replaces in the spring context the real repo with a mock that you can configure
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  ProductService productService;

  @Test
  void defaultsToUncategorizedWhenMissingCategory() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();
//    when(safetyClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safe", supplierId, null);

    productService.createProduct(dto);

    Product product = productRepo.findByName("name"); // RISK Unique?
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}

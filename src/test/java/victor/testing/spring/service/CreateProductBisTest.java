package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;

// another test class using an IDENTICAL Spring Config
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("wiremock")
@Transactional
public class CreateProductBisTest extends IntegrationTest {
  @Autowired
  ProductRepo repo;
  @Autowired
  ProductService service;

  @Test
  void throwsForUnsafeProduct() {
    ProductDto dto = new ProductDto("product-name", "sku-unsafe", HOME);

    assertThatThrownBy(() -> service.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }
}

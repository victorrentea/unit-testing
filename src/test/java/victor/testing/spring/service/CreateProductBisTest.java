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

@AutoConfigureWireMock(port = 0)
@ActiveProfiles("wiremock")
//@Sql("classpath:/sql/cleanup.sql")
@Transactional
public class CreateProductBisTest extends IntegrationTest {
  @Autowired
  ProductRepo repo;
  @Autowired
  ProductService service;

//  @AfterEach
//  @BeforeEach
//  final void cleanup() {
//    repo.deleteAll();
//  }

  @Test
  void throwsForUnsafeProduct() {
    ProductDto dto = new ProductDto("product-name", "sku-unsafe", HOME);

    assertThatThrownBy(() -> service.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  @WithMockUser("jdoe")
  void happy() {
    ProductDto dto = new ProductDto("product-name", "sku-safe", HOME);

    Long newId = service.createProduct(dto);

    assertThat(newId).isNotNull();
    Product product = repo.findByName("product-name");
    assertThat(product.getName()).isEqualTo("product-name");
    assertThat(product.getSku()).isEqualTo("sku-safe");
    assertThat(product.getCategory()).isEqualTo(HOME);
     assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic
    assertThat(product.getCreatedBy()).isEqualTo("jdoe"); // field set via Spring Magic
  }

  @Test
  @WithMockUser("jdoe")
  void defaultToUncategorizedForMissingCategory() {
    ProductDto dto = new ProductDto("product-name2", "sku-safe", null);

    service.createProduct(dto);

    Product product = repo.findByName("product-name2");
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }
}

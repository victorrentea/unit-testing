package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.api.dto.ProductDto;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;

@AutoConfigureWireMock(port = 0)
@ActiveProfiles("wiremock")
//@Sql("classpath:/sql/cleanup.sql") //#2
@Transactional //#3
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // #4 - time waste
public class CreateProductTest extends IntegrationTest {
  @Autowired
  ProductRepo repo;
  @Autowired
  ProductService service;

//  @AfterEach
//  @BeforeEach
//  final void cleanup() {
//    repo.deleteAll(); //#1
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
    ProductDto dto = new ProductDto("product-name", "sku-safe", null);

    service.createProduct(dto);

    Product product = repo.findByName("product-name");
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }
}

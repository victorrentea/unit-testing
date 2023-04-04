package victor.testing.spring.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.TestcontainersUtils;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

@Testcontainers
@SpringBootTest
@ActiveProfiles("db-migration")
public class ProductServiceTest {
  @MockBean
  public SafetyClient mockSafetyClient;
  @Autowired
  private ProductRepo productRepo;
  @Autowired
  private SupplierRepo supplierRepo;
  @Autowired
  private ProductService productService;

  @Container
  static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
          "postgres:11")
          .withReuse(true);

  @BeforeAll
  public static void startTestcontainer() {
    System.out.println("Starting testcontainer");
    postgres.start();
  }

  @AfterEach
  public void method() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }


  @DynamicPropertySource
  public static void registerPgProperties(DynamicPropertyRegistry registry) {
    TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
  }

  @Test
  public void createThrowsForUnsafeProduct() {
    when(mockSafetyClient.isSafe("bar")).thenReturn(false);

    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);
    assertThatThrownBy(() -> productService.createProduct(dto))
            .isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void createOk() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    // WHEN
    Long id = productService.createProduct(dto);

    // THEN
    Product product = productRepo.findById(id).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
  }

}

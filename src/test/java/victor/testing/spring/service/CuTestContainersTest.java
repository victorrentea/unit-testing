package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
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

@SpringBootTest
//@ActiveProfiles("db-mem")
@Transactional
@ActiveProfiles("db-migration")
@Testcontainers
// niciodata pe Jenkins
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

//@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CuTestContainersTest {
   @MockBean
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;
   @Container
   static public PostgreSQLContainer<?> postgres =
           new PostgreSQLContainer<>("postgres:11")
           .withDatabaseName("prop")
           .withUsername("postgres")
           .withPassword("password");

   @DynamicPropertySource
   public static void registerPgProperties(DynamicPropertyRegistry registry) {
      // A: if you want to spy the JDBC calls
      registry.add("spring.datasource.url", () -> TestcontainersUtils.injectP6SPYInJdbcUrl(postgres.getJdbcUrl()));
      registry.add("spring.datasource.driver-class-name", () -> "com.p6spy.engine.spy.P6SpyDriver");

      // B: clean (no spying)
      // registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
      // registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

      registry.add("spring.datasource.username", postgres::getUsername);
      registry.add("spring.datasource.password", postgres::getPassword);
   }

//   @BeforeEach
//   public void stergColacu() {
//      productRepo.deleteAll(); // ordinea conteaza de aia pe app mari curatarea DB e o arta (CASCADE .., DISABLE CONSTRAINTS)
//      supplierRepo.deleteAll();
//   }
   @Test
   public void createThrowsForUnsafeProduct() {
      when(mockSafetyClient.isSafe("bar")).thenReturn(false);

      ProductDto dto = new ProductDto("name", "bar", -1L, ProductCategory.HOME);
      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void tataLor() {
      // GIVEN
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }
   @Test
   public void tataLorBis() {
      // GIVEN
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }
}

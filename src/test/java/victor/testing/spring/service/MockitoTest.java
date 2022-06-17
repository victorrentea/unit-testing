package victor.testing.spring.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
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
import victor.testing.tools.WireMockExtension;

import java.io.IOException;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
//@ConditionaOn
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
//(properties = "spring.datasource.url=jdbc:h2:mem:")
//@ActiveProfiles("db-mem")
@Testcontainers
@Transactional // LOVE❤️
//@Preven
//@Tag("")
//@Sql("classpath:/sql/cleanup.sql")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // nu are voie sa apara pe git. RUPE SPringu de tot
public class MockitoTest extends AbstractRepoTestBase {
//   @MockBean // inlocuieste in contextu spring beanul real cu un Mock mockito pe care apoi ti-l si da aici sa-l programezik
//   public SafetyClient mockSafetyClient;// = new SafetyClient(new RestTemplate());
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @RegisterExtension
   public WireMockExtension wireMock = new WireMockExtension(9999);

   @Container
   static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
           .withDatabaseName("prop")
           .withUsername("postgres")
           .withPassword("password");

//   @SneakyThrows
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
//throw new IOException();
   }

//@BeforeEach
//final void before() {
//   // ordinea conteaza :)
//    productRepo.deleteAll();
//   supplierRepo.deleteAll();
//}
   @Test
   public void createThrowsForUnsafeProduct() {
//      when(mockSafetyClient.isSafe("bar")).thenReturn(false);

      ProductDto dto = new ProductDto("name", "bar", -1L, ProductCategory.HOME);
      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      // GIVEN
//      Supplier supplier = new Supplier().setId(13L);
      Long supplierId = supplierRepo.save(new Supplier()).getId();

//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
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
   public void createOk2() {
      // GIVEN
//      Supplier supplier = new Supplier().setId(13L);
      Long supplierId = supplierRepo.save(new Supplier()).getId();

//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
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

package victor.testing.spring.service.subp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.DBTest;
import victor.testing.spring.service.OtherService;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.WireMockExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static victor.testing.spring.domain.ProductCategory.*;

// AVOID this in normal applications: only use when you write code
// that CHANGES the spring context (eg defines new beans, changes environme)
// @DirtiesContext(classMode =  ClassMode.BEFORE_EACH_TEST_METHOD)
// tempting to use this to blow up your H2 in memory db.

//@WipeDB // NOT PARALLELIZABLE
@Transactional // parallelization-friendly

@SpringBootTest(properties = "safety.service.url.base=http://localhost:${wiremock.server.port}")
@ActiveProfiles("db-migration")
@Testcontainers
@AutoConfigureWireMock(port = 0)
// assume we can't do this: @Async , REQUIRES_NEW, no-sql, MQ, files on disk
public class ProductServiceTest extends DBTest {
   @Autowired
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;
//   @RegisterExtension
//   public WireMockExtension wireMock = new WireMockExtension(9999);

   @MockBean
   OtherService service;
//   @BeforeEach
//   final void before() {
//       productRepo.deleteAll(); // FK violation
//      supplierRepo.deleteAll();
//   }
   @Test
   public void createThrowsForUnsafeProduct() {
      // tell .isSave() to return false when called from production code
//      when(mockSafetyClient.isSafe("bar")).thenReturn(false);
      ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      // GIVEN (setup)
//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN (prod call)
      productService.createProduct(dto);

      // THEN (expectations/effects)
      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

   @Test
   public void createOkBis() {
      // GIVEN (setup)
//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN (prod call)
      productService.createProduct(dto);

      // THEN (expectations/effects)
      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}

package victor.testing.spring.service.subpacket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.web.dto.ProductDto;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@ActiveProfiles("db-mem")
@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // only use for debugging.
// If you nuke spring, you just lost ~40 seconds of your build time. for you and your colleagues.\
// NOT on GIT.
//@Sql(value = "classpath:/sql/cleanup.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ProductServiceTest extends DBTestBase {
   @MockBean // replaces the real class with a mockito mock that you can then configur
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;
   private Long supplierId;

   // in what scenario, starting a Tx at the benning of the test is NOT ENOUGH,
   // and data inserted from Prod will still remain despite the ROLLBACK of this test transaction?
   // 1) @Transactional(propagation=
   // 2) multithreading
   // if that happens, => @Sql or @Before+AFter cleanup + no @Transactional on class


   //   @BeforeEach
//   public void flushAfterUse() {
//
////      /celean the whole DB
//      productRepo.deleteAll();
//      supplierRepo.deleteAll();
//   }

//   @Test
//   public void createThrowsForUnsafeProduct() {
//      when(mockSafetyClient.isSafe("bar")).thenReturn(false);
//
//      ProductDto dto = new ProductDto("name", "bar", -1L, ProductCategory.HOME);
//      assertThatThrownBy(() -> productService.createProduct(dto))
//              .isInstanceOf(IllegalStateException.class);
//   }

   @BeforeEach
   final void before() {
      supplierId = supplierRepo.save(new Supplier()).getId();

   }
   @Test
   public void createOk() {
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

      // WHEN
     productService.createProduct(dto);

      assertThat(productRepo.count()).isEqualTo(1);
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }
   @Test
   public void createOk2() {
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

      // WHEN
     productService.createProduct(dto);

      assertThat(productRepo.count()).isEqualTo(1);
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}

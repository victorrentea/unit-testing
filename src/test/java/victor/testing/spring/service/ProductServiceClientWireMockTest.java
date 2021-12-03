package victor.testing.spring.service;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.WireMockExtension;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.byLessThan;

@Transactional
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@ActiveProfiles("db-mem") // CONNECT BY  ;  /*+ hints in some SQL */  DBMS.STD_FCT_FROM_ORACLE
public class ProductServiceClientWireMockTest {
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;
   @RegisterExtension
   public  WireMockExtension wireMock = new WireMockExtension(9999);

   @Test
   public void createThrowsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
//         when(mockSafetyClient.isSafe("bar")).thenReturn(false);
         productService.createProduct(new ProductDto("name", "bar",-1L, ProductCategory.HOME));
      });
   }

   @Test
   public void createOk() {
      Supplier supplier = supplierRepo.save(new Supplier());

//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      productService.createProduct(new ProductDto("name", "safebar", supplier.getId(), ProductCategory.HOME));

      org.assertj.core.api.Assertions.assertThat(productRepo.count()).isEqualTo(1);
      Product product = productRepo.findAll().get(0);

      try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
         softly.assertThat(product.getName()).isEqualTo("name");
         softly.assertThat(product.getBarcode()).isEqualTo("safebar");
         softly.assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
         softly.assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
         softly.assertThat(product.getCreateDate()).isNotNull();
         softly.assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(5, SECONDS));
      }
   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}

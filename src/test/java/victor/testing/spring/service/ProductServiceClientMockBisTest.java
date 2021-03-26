package victor.testing.spring.service;

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
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.web.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@ActiveProfiles({"db-mem"})
public class ProductServiceClientMockBisTest {
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @RegisterExtension
   public WireMockExtension wireMockExtension = new WireMockExtension(9999);

   @Test
   public void throwsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         productService.createProduct(new ProductDto("name", "unsafe",-1L, ProductCategory.HOME));
      });
   }

   @Test
   public void fullOk() {
      Supplier supplier = new Supplier();
      long supplierId = supplierRepo.save(supplier).getId();

      long productId = productService.createProduct(new ProductDto("name", "safe", supplierId, ProductCategory.HOME));

      Product product = productRepo.findById(productId).get();

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getUpc()).isEqualTo("safe");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isNotNull();
   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}

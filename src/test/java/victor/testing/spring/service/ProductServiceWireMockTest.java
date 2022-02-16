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
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.WireMockExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "safety.service.url.base=http://localhost:8089")
@ActiveProfiles("db-mem")
@Transactional
public class ProductServiceWireMockTest {
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @RegisterExtension
   public WireMockExtension wireMock = new WireMockExtension(8089);

   @Test
   public void throwsForUnsafeProduct() {

      Assertions.assertThrows(IllegalStateException.class, () -> {
         productService.createProduct(new ProductDto("name", "bar", -1L, ProductCategory.HOME));
      });
   }

   @Test
   public void fullOk() {
      long supplierId = supplierRepo.save(new Supplier()).getId();

      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);
      productService.createProduct(dto);

      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);

      assertThat(product.getCreateDate()); // TODO test time
   }
}

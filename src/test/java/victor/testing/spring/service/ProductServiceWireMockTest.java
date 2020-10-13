package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.client.WireMock;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@ActiveProfiles("db-mem")
@Transactional
class ProductServiceWireMockTest {

   @Autowired
   private ProductService productService;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductRepo productRepo;

   @RegisterExtension
   public WireMockExtension wireMock = new WireMockExtension(9999);

   @Test
   public void createProduct() {
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("Ceburashka", "SAFE", supplierId, ProductCategory.HOME);
      long productId = productService.createProduct(dto);
      Product product = productRepo.findById(productId).get();
      assertThat(product.getName()).isEqualTo("Ceburashka");
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getUpc()).isEqualTo("SAFE");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCreateDate()).isNotNull(); // ingineresti
   }
   @Test
   public void createProductFailsNotSafe() {
      assertThrows(IllegalStateException.class,
          () ->productService.createProduct(new ProductDto("Ceburashka", "UNSAFE", -1L, ProductCategory.HOME)));
   }
}
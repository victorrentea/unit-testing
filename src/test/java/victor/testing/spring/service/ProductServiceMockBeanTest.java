package victor.testing.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
class ProductServiceMockBeanTest {

   @Autowired
   private ProductService productService;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductRepo productRepo;
   @MockBean
   private SafetyClient safetyClient;

   @Test
   void createProduct() {
      LocalDateTime t0 = LocalDateTime.now();
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      when(safetyClient.isSafe("1")).thenReturn(true);
      ProductDto dto = new ProductDto("Ceburashka", "1", supplierId, ProductCategory.HOME);
      long productId = productService.createProduct(dto);
      Product product = productRepo.findById(productId).get();
      assertThat(product.getName()).isEqualTo("Ceburashka");
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getUpc()).isEqualTo("1");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCreateDate()).isNotNull(); // ingineresti


      assertThat(product.getCreateDate().truncatedTo(ChronoUnit.DAYS))
          .isEqualTo(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)); // ingineresti

      assertThat(product.getCreateDate()).isAfterOrEqualTo(t0).isBeforeOrEqualTo(LocalDateTime.now()); // fitza

   }
}
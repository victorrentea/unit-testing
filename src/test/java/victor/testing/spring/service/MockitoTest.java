package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
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

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ActiveProfiles("db-mem")
public class MockitoTest {
   @MockBean
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @Test
   public void createThrowsForUnsafeProduct() {
      when(mockSafetyClient.isSafe("bar")).thenReturn(false);

      assertThrows(IllegalStateException.class, () ->
          productService.createProduct(new ProductDto("name", "bar", -1L, ProductCategory.HOME)));
   }

   @Test
   public void createOk() {
      // GIVEN
      Supplier supplier = supplierRepo.save(new Supplier());
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      // WHEN
      ProductDto dto = new ProductDto("name", "safebar",
          supplier.getId(),
          ProductCategory.HOME);
      productService.createProduct(dto);

      // THEN
//      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
//      verify(productRepo).save(productCaptor.capture());
//      Product product = productCaptor.getValue();
      org.assertj.core.api.Assertions.assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }
   @Test
   public void createOk2() {
      // GIVEN
      Supplier supplier = supplierRepo.save(new Supplier());
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      // WHEN
      ProductDto dto = new ProductDto("name", "safebar",
          supplier.getId(),
          ProductCategory.HOME);
      productService.createProduct(dto);

      // THEN
//      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
//      verify(productRepo).save(productCaptor.capture());
//      Product product = productCaptor.getValue();
      org.assertj.core.api.Assertions.assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}

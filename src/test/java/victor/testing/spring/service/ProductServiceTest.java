package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
   public static final long SUPPLIER_ID = 13L;
   @Mock
   public SafetyClient mockSafetyClient;
   @Mock
   private ProductRepo productRepo;
   @Mock
   private SupplierRepo supplierRepo;
   @InjectMocks
   private ProductService productService;

   @Test
   public void createThrowsForUnsafeProduct() {
      when(mockSafetyClient.isSafe("bar")).thenReturn(false);
      ProductDto dto = new ProductDto("name", "bar", -1L, ProductCategory.HOME);

      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      Supplier supplier = new Supplier().setId(SUPPLIER_ID);
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      when(supplierRepo.getReferenceById(SUPPLIER_ID)).thenReturn(supplier);
      ProductDto dto = new ProductDto("name", "safebar", SUPPLIER_ID, ProductCategory.HOME);

      // WHEN
      productService.createProduct(dto);

      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
      verify(productRepo).save(productCaptor.capture());
      Product product = productCaptor.getValue();
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(SUPPLIER_ID);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}

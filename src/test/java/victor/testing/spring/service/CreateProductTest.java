package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.HOME;
//TODO:
// 0) scritzi-va pe prezenta
// 0) faceti-va name-tags cu foile indoite si markerul din sala
// A) clonati/download zip la :
   //Branch: db_2023_05_spring
   //git: https://github.com/victorrentea/unit-testing.git
//-sau-
//B) cereti USB stickul :)
@SpringBootTest
public class CreateProductTest {
   @MockBean
   public SafetyClient mockSafetyClient;
   @MockBean
   private ProductRepo productRepo;
   @MockBean
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @Test
   public void createThrowsForUnsafeProduct() {
      when(mockSafetyClient.isSafe("bar")).thenReturn(false);

      ProductDto dto = new ProductDto("name", "bar", -1L, HOME);
      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      // GIVEN
      Supplier supplier = new Supplier().setId(13L);
      when(supplierRepo.findById(supplier.getId())).thenReturn(Optional.of(supplier));
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplier.getId(), HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
      verify(productRepo).save(productCaptor.capture());
      Product product = productCaptor.getValue();

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
      assertThat(product.getCategory()).isEqualTo(HOME);
      // assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS)); // uses Spring Magic
   }

}

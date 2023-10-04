package victor.testing.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.service.ProductService;
import victor.testing.spring.product.api.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;

@ExtendWith(MockitoExtension.class)
public class CreateProductTest {
  @Mock
  ProductRepo productRepo;
  @Mock
  SafetyClient safetyClient;
  @InjectMocks
  ProductService productService;
  @Captor
  ArgumentCaptor<Product> productCaptor;

  @Test
  void throwsForUnsafeProduct() {
    when(safetyClient.isSafe("sku-unsafe"))
        .thenReturn(false);
    ProductDto dto = new ProductDto("product-name", "sku-unsafe", HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void happy() {
    when(safetyClient.isSafe("sku-safe"))
        .thenReturn(true);
    when(productRepo.save(any())).thenReturn(42L);
    ProductDto dto = new ProductDto("product-name", "sku-safe", HOME);

    Long newId = productService.createProduct(dto);

    assertThat(newId).isNotNull();
    verify(productRepo).save(productCaptor.capture());
    Product product = productCaptor.getValue();
    assertThat(product.getName()).isEqualTo("product-name");
    assertThat(product.getSku()).isEqualTo("sku-safe");
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic
//    assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
  }

}

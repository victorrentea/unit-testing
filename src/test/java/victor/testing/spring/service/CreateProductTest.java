package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.api.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

@ExtendWith(MockitoExtension.class)
public class CreateProductTest {
  @Mock
  ProductRepo repo;
  @Mock
  SafetyClient safetyClient;
  @InjectMocks
  ProductService service;
  @Captor
  ArgumentCaptor<Product> captor;

  @Test
  void throwsForUnsafeProduct() {
    when(safetyClient.isSafe("sku-unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("product-name", "sku-unsafe", HOME);

    assertThatThrownBy(() -> service.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void happy() {
    when(safetyClient.isSafe("sku-safe")).thenReturn(true);
    when(repo.save(any())).thenReturn(new Product().setId(42L));
    ProductDto dto = new ProductDto("product-name", "sku-safe", HOME);

    Long newId = service.createProduct(dto);

    assertThat(newId).isNotNull();
    verify(repo).save(captor.capture());
    Product product = captor.getValue();
    assertThat(product.getName()).isEqualTo("product-name");
    assertThat(product.getSku()).isEqualTo("sku-safe");
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic
//    assertThat(product.getCreatedBy()).isEqualTo("jdoe"); // field set via Spring Magic
  }

}

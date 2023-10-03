package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.domain.Supplier;
import victor.testing.design.app.repo.ProductRepo;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static victor.testing.design.app.domain.ProductCategory.HOME;

public class GetProductSocialTest {
  ProductRepo productRepoMock = mock(ProductRepo.class);

  ProductService productService = new ProductService(productRepoMock, new ProductMapper());
  @Test
  void service_plus_mapper() {
    Product product = new Product()
        .setId(1L)
        .setSupplier(new Supplier().setId(2L))
        .setName("name")
        .setSku("sku")
        .setCategory(HOME)
        .setCreatedDate(LocalDate.now());
    when(productRepoMock.findById(1L)).thenReturn(Optional.of(product));

    ProductDto dto = productService.getProduct(1L);

    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getSupplierId()).isEqualTo(2L);
    assertThat(dto.getName()).isEqualTo("name");
    assertThat(dto.getSku()).isEqualTo("sku");
    assertThat(dto.getCategory()).isEqualTo(HOME);
    assertThat(dto.getCreatedDate()).isEqualTo(LocalDate.now());
  }
}

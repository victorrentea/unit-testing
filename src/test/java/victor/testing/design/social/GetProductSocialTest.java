package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.domain.Supplier;
import victor.testing.design.social.ProductDto;
import victor.testing.design.app.repo.ProductRepo;
import victor.testing.design.social.ProductMapper;
import victor.testing.design.social.ProductService;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static victor.testing.design.app.domain.ProductCategory.HOME;

public class GetProductSocialTest {
  private ProductRepo productRepo = mock(ProductRepo.class);
  private ProductService productService = new ProductService(
      productRepo,
      new ProductMapper());

  @Test
  void service_plus_mapper() {
    LocalDate date = LocalDate.now();
    Product product = new Product()
            .setId(1L)
            .setName("name")
            .setSku("SKU")
            .setCategory(HOME)
            .setCreatedDate(date)
            .setSupplier(new Supplier().setId(2L));
    when(productRepo.findById(1L)).thenReturn(Optional.of(product));

    ProductDto dto = productService.getProduct(1L);

    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getName()).isEqualTo("name");
    assertThat(dto.getSku()).isEqualTo("SKU");
    assertThat(dto.getCategory()).isEqualTo(HOME);
    assertThat(dto.getCreatedDate()).isEqualTo(date);
    assertThat(dto.getSupplierId()).isEqualTo(2L);
  }

}

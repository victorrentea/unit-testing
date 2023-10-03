package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.domain.Supplier;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.design.app.domain.ProductCategory.HOME;

public class ProductMapperSTest {
  @Test
  void mapperTest() { // AI this
    Product product = new Product()
        .setId(1L)
        .setSupplier(new Supplier().setId(2L))
        .setName("name")
        .setSku("sku")
        .setCategory(HOME)
        .setCreatedDate(LocalDate.now());

    ProductMapper productMapper = new ProductMapper();

    ProductDto dto = productMapper.toDto(product);

    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getSupplierId()).isEqualTo(2L);
    assertThat(dto.getName()).isEqualTo("name");
    assertThat(dto.getSku()).isEqualTo("sku");
    assertThat(dto.getCategory()).isEqualTo(HOME);
    assertThat(dto.getCreatedDate()).isEqualTo(LocalDate.now());

  }
}

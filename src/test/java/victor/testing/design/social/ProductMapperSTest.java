package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import victor.testing.design.app.domain.Supplier;
import victor.testing.design.app.domain.Product;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static victor.testing.design.app.domain.ProductCategory.HOME;

public class ProductMapperSTest {

  @Test
  void mapperTest() {
    LocalDate date = LocalDate.now();
    Product product = new Product()
            .setId(1L)
            .setName("name")
            .setSku("SKU")
            .setCategory(HOME)
            .setCreatedDate(date)
            .setSupplier(new Supplier().setId(2L));

    ProductDto dto = new ProductMapper().toDto(product);

    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getName()).isEqualTo("name");
    assertThat(dto.getSku()).isEqualTo("SKU");
    assertThat(dto.getCategory()).isEqualTo(HOME);
    assertThat(dto.getCreatedDate()).isEqualTo(date);
    assertThat(dto.getSupplierId()).isEqualTo(2L);
  }
}

package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.domain.ProductCategory;
import victor.testing.design.app.domain.Supplier;
import victor.testing.design.app.repo.ProductRepo;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceSTest {
  @Test
  void whatAmITestingHere() {
    ProductRepo productRepoMock = mock(ProductRepo.class);
    Product product = new Product();
    when(productRepoMock.findById(1L)).thenReturn(of(product));
    ProductMapper productMapperMock = mock(ProductMapper.class);
    ProductDto dto = new ProductDto();
    when(productMapperMock.toDto(product)).thenReturn(dto);

    ProductService productService = new ProductService(
        productRepoMock,
        productMapperMock);

    ProductDto result = productService.getProduct(1L);

    assertThat(result).isSameAs(dto);
  }

}

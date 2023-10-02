package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.domain.Supplier;
import victor.testing.design.app.repo.ProductRepo;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceSTest {
  @Test
  void whatAmITestingHere() {
    // DON'T DO THIS: mocking the mapper out
    ProductRepo repoMock = mock(ProductRepo.class);
    Product product = new Product().setSupplier(new Supplier());
    when(repoMock.findById(1L)).thenReturn(of(product));
    ProductMapper mapperMock = mock(ProductMapper.class);
    ProductDto dto = new ProductDto();
    when(mapperMock.toDto(product)).thenReturn(dto);
    ProductService productService = new ProductService(repoMock, mapperMock);

    ProductDto returnedDto = productService.getProduct(1L);

    assertThat(returnedDto).isSameAs(dto);
  }

}

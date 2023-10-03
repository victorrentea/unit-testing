package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.repo.ProductRepo;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceSTest {
  @Mock
  ProductRepo productRepo;
  @InjectMocks
  ProductService productService;
  @Mock
  ProductMapper productMapper;
  @Test
  void whatAmITestingHere() {
    Product product = new Product();
    when(productRepo.findById(1L)).thenReturn(Optional.of(product));
    when(productMapper.toDto(product)).thenReturn(new ProductDto());

    ProductDto dto = productService.getProduct(1L);
    verify(productMapper).toDto(product);
  }
}

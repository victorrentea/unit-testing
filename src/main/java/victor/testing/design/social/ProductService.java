package victor.testing.design.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.repo.ProductRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepo productRepo;
  private final ProductMapper productMapper;

  public ProductDto getProduct(long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    return productMapper.toDto(product);
  }
}

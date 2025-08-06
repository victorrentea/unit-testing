package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.rest.dto.ProductDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetProductService {
  private final ProductRepo productRepo;
  private final ProductMapper productMapper;

  public ProductDto getProduct(long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    if (productRepo.countByName(product.getName()) >= 1) {
      throw new IllegalStateException("Multiple products with the same name: " + product.getName());
    }
    return productMapper.toDto(product);
  }



}

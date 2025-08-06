package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.entity.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.rest.dto.ProductDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetProductService {
  private final ProductRepo productRepo;
  private final ProductMapper productMapper;
  @Value("${prop.from.file}")
  private Integer prop;

//  @Cacheable @Transactional CAN'T BE TESTED with @ContextConfiguration
  public ProductDto getProduct(long productId) {
    System.out.println("Something useful with "+ prop);
    Product product = productRepo.findById(productId).orElseThrow();
    if (productRepo.countByName(product.getName()) >=prop) {
      throw new IllegalStateException("Multiple products with the same name: " + product.getName());
    }
    return productMapper.toDto(product);
  }



}

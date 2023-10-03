package victor.testing.spring.product.service;

import org.springframework.stereotype.Component;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.api.dto.ProductDto;

@Component
public class ProductMapper {
  public ProductDto toDto(Product product) {
    return new ProductDto()
            .setId(product.getId())
            .setName(product.getName())
            .setSku(product.getSku())
            .setCategory(product.getCategory())
            .setCreatedDate(product.getCreatedDate());
  }
}

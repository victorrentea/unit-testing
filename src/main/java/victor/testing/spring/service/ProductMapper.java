package victor.testing.spring.service;

import org.springframework.stereotype.Component;
import victor.testing.spring.domain.Product;
import victor.testing.spring.api.dto.ProductDto;

@Component
public class ProductMapper {
  public ProductDto toDto(Product product) {
    return new ProductDto()
            .setId(product.getId())
            .setSupplierId(product.getSupplier().getId())
            .setName(product.getName())
            .setUpc(product.getUpc())
            .setCategory(product.getCategory())
            .setCreatedDate(product.getCreatedDate());
  }
}

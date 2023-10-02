package victor.testing.design.social;

import org.springframework.stereotype.Component;
import victor.testing.design.app.domain.Product;

@Component
public class ProductMapper {
  public ProductDto toDto(Product product) {
    return new ProductDto()
            .setId(product.getId())
            .setSupplierId(product.getSupplier().getId())
            .setName(product.getName())
            .setSku(product.getSku())
            .setCategory(product.getCategory())
            .setCreatedDate(product.getCreatedDate());
  }
}

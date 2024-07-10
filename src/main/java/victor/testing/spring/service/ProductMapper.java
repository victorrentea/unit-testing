package victor.testing.spring.service;

import org.springframework.stereotype.Component;
import victor.testing.spring.domain.Product;
import victor.testing.spring.api.dto.ProductDto;

@Component
public class ProductMapper {
  public ProductDto toDto(Product product, String currentUsername) {
    return new ProductDto()
            .setId(product.getId())
            .setSupplierCode(product.getSupplier().getCode())
            .setName(product.getName())
            .setBarcode(product.getBarcode())
            .setCategory(product.getCategory())
            .setCreatedDate(product.getCreatedDate());
  }
}

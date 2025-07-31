package victor.testing.spring.service;

import org.springframework.stereotype.Component;
import victor.testing.spring.entity.Product;
import victor.testing.spring.rest.dto.ProductDto;

@Component
public class ProductMapper {
  public ProductDto toDto(Product product) {
    return ProductDto.builder()
        .id(product.getId())
        .supplierCode(product.getSupplier().getCode())
        .name(product.getName())
        .barcode(product.getBarcode())
        .category2(product.getCategory())
        .createdDate(product.getCreatedDate())
        .build();
  }
}

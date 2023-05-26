package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

@RequiredArgsConstructor
@Component
public class ProductMapper {
  private final SupplierRepo supplierRepo;

  public ProductDto toDto(Product product) {
    return new ProductDto()
            .setId(product.getId())
            .setSupplierId(product.getSupplier().getId())
            .setName(product.getName())
            .setBarcode(product.getBarcode())
            .setCategory(product.getCategory())
            .setCreateDate(product.getCreateDate());
  }

  public Product fromDto(ProductDto productDto) {
    Product product = new Product();
    product.setName(productDto.getName());
    product.setBarcode(productDto.getBarcode());
    product.setCategory(productDto.getCategory());
    product.setSupplier(supplierRepo.findById(productDto.getSupplierId()).orElseThrow());
    return product;
  }
}

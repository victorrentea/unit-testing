package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.api.dto.ProductSearchCriteria;
import victor.testing.spring.api.dto.ProductSearchResult;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
  private final SupplierRepo supplierRepo;
  private final ProductRepo productRepo;
  private final SafetyClient safetyClient;
  private final ProductMapper productMapper;

  public long createProduct(ProductDto productDto) {
    log.info("Creating product {}", productDto.getUpc());
    boolean safe = safetyClient.isSafe(productDto.getUpc()); // ⚠️ REST call inside
    if (!safe) {
      throw new IllegalStateException("Product is not safe!");
    }
    if (productDto.getCategory() == null) {
      productDto.setCategory(ProductCategory.UNCATEGORIZED); // untested line 😱
    }
    Product product = new Product();
    product.setName(productDto.getName());
    product.setUpc(productDto.getUpc());
    product.setCategory(productDto.getCategory());
    product.setSupplier(supplierRepo.findByName(productDto.getSupplierName()));
    productRepo.save(product);
    return product.getId();
  }

  public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
    return productRepo.search(criteria);
  }

  public ProductDto getProduct(long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    return productMapper.toDto(product);
  }
}

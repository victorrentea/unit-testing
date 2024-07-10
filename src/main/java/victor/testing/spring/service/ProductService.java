package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.api.dto.ProductSearchCriteria;
import victor.testing.spring.api.dto.ProductSearchResult;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import javax.inject.Inject;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
// lombok generates a consturctor with all final fields
public class ProductService {
  public static final String PRODUCT_CREATED_TOPIC = "product-created";
  private final SupplierRepo supplierRepo;
  private final ProductRepo productRepo;
  private final SafetyApiClient safetyApiClient;
  private final ProductMapper productMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void createProduct(ProductDto productDto) {
    log.info("Creating product " + productDto.getBarcode());
    boolean safe = safetyApiClient.isSafe(productDto.getBarcode());
    if (!safe) {
      throw new IllegalStateException("Product is not safe!");
    }
    if (productDto.getCategory() == null) {
      productDto.setCategory(ProductCategory.UNCATEGORIZED);
    }
    Product product = new Product();
    product.setName(productDto.getName());
//    product.setBarcode(productDto.getBarcode());
//    product.setCategory(productDto.getCategory());
    product.setSupplier(supplierRepo.findByCode(productDto.getSupplierCode()).orElseThrow());
    productRepo.save(product);
    kafkaTemplate.send(PRODUCT_CREATED_TOPIC, "k", product.getName().toUpperCase());
  }

  public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
    return productRepo.search(criteria);
  }

  public ProductDto getProduct(long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    return productMapper.toDto(product);
  }
}

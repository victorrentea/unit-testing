package victor.testing.spring.service;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.api.dto.ProductSearchCriteria;
import victor.testing.spring.api.dto.ProductSearchResult;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

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
    if (productRepo.countByName(productDto.getName()) !=0) {
      throw new IllegalArgumentException("Product already exists: " + productDto.getName());
    }
    if (productDto.getCategory() == null) {
      productDto.setCategory(ProductCategory.UNCATEGORIZED);
    }
    Product product = newProduct(productDto);
    Supplier supplier = supplierRepo.findByCode(productDto.getSupplierCode()).orElseThrow();
    product.setSupplier(supplier);
    productRepo.save(product);
    kafkaTemplate.send(PRODUCT_CREATED_TOPIC, "k", product.getName().toUpperCase());
  }

  // we are CHEATING: breaking the encapsulation of the old class(2000), JUST FOR TESTING
  // I DON"T GIVE A SHIT ABOUT THE ENCAPSULATION OF SUCH A BEAST (2000 loc).
  // it is far more important to bring it under automated tests than preserving encapsulated garbage.

  /*private*/
  @VisibleForTesting
  Product newProduct(ProductDto productDto) {
    Product product = new Product();
    log.error("THIS SHOULD NOT EXECUTE ");
    product.setName(productDto.getName());
    product.setBarcode(productDto.getBarcode());
    product.setCategory(productDto.getCategory());
    // Imagine dragons. calls to 10 other methods, calls to repos,
    // old unknown heavy logic
    return product;
  }

  public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
    return productRepo.search(criteria);
  }

  public ProductDto getProduct(long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    if (product.getCategory()==ProductCategory.KIDS) {
//      ..
    }
    return productMapper.toDto(product/*, "user"*/);
    // the longer the tests are, the more robust against refactoring they are.
    // the closer they get to the functional requirement
  }
}

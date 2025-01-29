package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
  public static final String PRODUCT_CREATED_TOPIC = "product-created";
  private final SupplierRepo supplierRepo;
  private final ProductRepo productRepo;
  private final SafetyApiAdapter safetyApiAdapter;
  private final ProductMapper productMapper;
  private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

  public Long createProduct(ProductDto productDto) {
    log.info("Creating product {}", productDto);
    boolean safe = safetyApiAdapter.isSafe(productDto.getBarcode()); // ⚠️ REST call inside
    if (!safe) { // check
      throw new IllegalStateException("Product is not safe!");
    }
    if (productDto.getCategory() == null) { // sanitizare de date
      productDto.setCategory(ProductCategory.UNCATEGORIZED);
    }
    Product product = new Product();
    product.setName(productDto.getName());
    product.setBarcode(productDto.getBarcode());
    product.setCategory(productDto.getCategory());
    product.setSupplier(supplierRepo.findByCode(productDto.getSupplierCode()).orElseThrow());
    Long productId = productRepo.save(product).getId(); // id atribuit de JPA din SEQUENCE
    ProductCreatedEvent event = new ProductCreatedEvent(productId, LocalDateTime.now());
    kafkaTemplate.send(PRODUCT_CREATED_TOPIC, "k", event);
    return productId;
  }

  public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
    return productRepo.search(criteria);
  }

  public ProductDto getProduct(long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    if (productRepo.countByName(product.getName()) > 1) {
      throw new IllegalStateException("Multiple products with the same name: " + product.getName());
    }
    return productMapper.toDto(product);
  }

  public void deleteProduct(long productId) {
    productRepo.deleteById(productId);
  }
}

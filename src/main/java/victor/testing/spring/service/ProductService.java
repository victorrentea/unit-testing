package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  private final SafetyApiAdapter safetyApiAdapter;
  private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  private final ProductRepo productRepo;
  private final ProductMapper productMapper;

  public Long createProduct(ProductDto productDto) {
    boolean safe = safetyApiAdapter.isSafe(productDto.barcode()); // ⚠️ REST call inside
    if (!safe) {
      log.warn("[ALARM-CALL-LEGAL] Tried to create an illegal product {}", productDto);
      throw new IllegalStateException("Product is not safe!");
    }
    if (productDto.category() == null) {
      productDto = productDto.withCategory(ProductCategory.UNCATEGORIZED);
    }
    Product product = new Product();
    product.setName(productDto.name());
    product.setBarcode(productDto.barcode());
    product.setCategory(productDto.category());
    product.setSupplier(supplierRepo.findByCode(productDto.supplierCode()).orElseThrow());
    Long productId = productRepo.save(product).getId();
    ProductCreatedEvent event = new ProductCreatedEvent(productId, LocalDateTime.now());
    kafkaTemplate.send(PRODUCT_CREATED_TOPIC, "k", event); // a 'tenant-id' message header is added by victor.testing.spring.config.AddTenantIdToSentMessagesInterceptor
    return productId;
  }







  public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
    return productRepo.search(criteria);
  }

  public void deleteProduct(long productId) {
    productRepo.deleteById(productId);
  }
}


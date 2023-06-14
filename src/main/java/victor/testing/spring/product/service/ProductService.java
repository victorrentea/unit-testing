package victor.testing.spring.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import victor.testing.filebased.message.KafkaMessageSender;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.ProductCategory;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.api.dto.ProductSearchCriteria;
import victor.testing.spring.product.api.dto.ProductSearchResult;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
  public static final String PRODUCT_CREATED_TOPIC = "product-created";
  private final SafetyClient safetyClient;
  private final ProductRepo productRepo;
  private final SupplierRepo supplierRepo;
  private final ProductMapper productMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void createProduct(ProductDto productDto) {
    boolean safe = safetyClient.isSafe(productDto.getBarcode()); // ⚠️ REST call inside
    if (!safe) {
      throw new IllegalStateException("Product is not safe: " + productDto.getBarcode());
    }
    if (productDto.getCategory() == null) {
      productDto.setCategory(ProductCategory.UNCATEGORIZED); // untested line 😱
    }
    Product product = new Product();
    product.setName(productDto.getName());
    product.setBarcode(productDto.getBarcode());
    product.setCategory(productDto.getCategory());
    product.setSupplier(supplierRepo.findById(productDto.getSupplierId()).orElseThrow());
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
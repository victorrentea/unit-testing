package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import scala.concurrent.impl.FutureConvertersImpl.CF;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
  private final SafetyClient safetyClient;
  private final ProductRepo productRepo;
  private final SupplierRepo supplierRepo;

//  @Async
  //@Transactional(propagation = Propagation.REQUIRES_NEW)
  public void createProduct(ProductDto productDto) {
    boolean safe = safetyClient.isSafe(productDto.barcode); // ⚠️ REST call inside
    if (!safe) {
      throw new IllegalStateException("Product is not safe: " + productDto.barcode);
    }
    Product product = new Product();
    product.setName(productDto.name);
    product.setBarcode(productDto.barcode);
    product.setCreateDate(LocalDateTime.now());
    product.setSupplier(supplierRepo.findById(productDto.supplierId).orElseThrow());
    if (productDto.category == null) {
      productDto.category = ProductCategory.UNCATEGORIZED; // untested 😱
    }
    product.setCategory(productDto.category);
    productRepo.save(product);
//    return product.getId();
  }

  public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
    return productRepo.search(criteria);
  }

  public ProductDto getProduct(long productId) {
    return new ProductDto(productRepo.findById(productId).orElseThrow());
  }
}

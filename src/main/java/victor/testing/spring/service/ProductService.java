package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
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
    @Value("${ceva.prop}")
    String cevaDinProps;

    public void createProduct(ProductDto productDto) {
        boolean safe = safetyClient.isSafe(productDto.barcode);
        if (!safe) {
            throw new IllegalStateException("Product is not safe: " + productDto.barcode);
        }
        if (productDto.category == null) {
            productDto.category = ProductCategory.HOME;
        }
        Product product = new Product();
        product.setName(productDto.name);
        product.setCategory(productDto.category);
        product.setBarcode(productDto.barcode);
        // TODO CR check that the supplier is active!
        product.setCreateDate(LocalDateTime.now());
        productRepo.save(product);
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }

    public boolean isActive(long productId) {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        return productRepo.findById(productId).get()
            .getCreateDate().isAfter(oneYearAgo);
    }
}

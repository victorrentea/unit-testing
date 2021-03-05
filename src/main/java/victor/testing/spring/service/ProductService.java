package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final SafetyClient safetyClient;
    private final ProductRepo productRepo;
    private final SupplierRepo supplierRepo;

    public long createProduct(ProductDto productDto) {
        boolean safe = safetyClient.isSafe(productDto.upc);
        if (!safe) {
            throw new IllegalStateException("Product is not safe: " + productDto.upc);
        }

        if (StringUtils.isBlank(productDto.name)) {
            throw new IllegalArgumentException();
        }
        Product product = new Product();
        product.setName(productDto.name);
        product.setCategory(productDto.category);
        product.setUpc(productDto.upc.toUpperCase());
        product.setSupplier(supplierRepo.findById(productDto.supplierId).get());
        productRepo.save(product);
        return product.getId();
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria).stream().map(ProductSearchResult::new).collect(Collectors.toList());

    }

    public boolean isActive(long productId) {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        return productRepo.findById(productId).get()
            .getCreateDate().isAfter(oneYearAgo);
    }
}

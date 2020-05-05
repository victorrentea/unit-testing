package ro.victor.unittest.spring.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.domain.ProductService;
import ro.victor.unittest.spring.repo.SupplierRepo;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.ProductDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final ProductRepo productRepo;

    public ProductDto getProduct(Long productId) {
        Product product = productService.getProduct(productId);
        ProductDto dto = new ProductDto(product);
        dto.sampleDate = product.getSampleDate().orElse(LocalDateTime.now()).toString();
        return dto;
    }

    public List<ProductSearchResult> searchPeace(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }
}

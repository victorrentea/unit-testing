package ro.victor.unittest.spring.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.service.ProductService;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.ProductDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final ProductRepo productRepo;
    private final Clock clock;

    public ProductDto getProduct(long productId) {
        Product product = productService.getProduct(productId);
        ProductDto dto = new ProductDto(product);
        dto.sampleDate = product.getSampleDate()
            .orElse(LocalDateTime.now(clock).minus(5, SECONDS))
            .toString();
        return dto;
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }
}

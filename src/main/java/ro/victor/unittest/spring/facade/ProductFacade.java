package ro.victor.unittest.spring.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.ProductService;
import ro.victor.unittest.spring.repo.FileRepo;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.ProductDto;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final ProductRepo productRepo;
    private final Clock clock;
    private final FileRepo fileRepo;

//    @Transactional(propagation = Propagation.REQUIRES_NEW) // deschide o noua Tx pe o conex noua luata din pool
    public ProductDto getProduct(long productId) {
        Product product = productService.getProduct(productId);
        ProductDto dto = new ProductDto(product);
        System.out.println("FILES: " + fileRepo.getFilesInInputFolder());
        dto.sampleDate = product.getSampleDate().orElse(LocalDateTime.now(clock)).format(DateTimeFormatter.ISO_DATE_TIME);
        return dto;
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }
}

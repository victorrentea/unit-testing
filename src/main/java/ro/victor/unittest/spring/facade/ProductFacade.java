package ro.victor.unittest.spring.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Label;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.ProductService;
import ro.victor.unittest.spring.infra.LabelWebServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.ProductDto;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final ProductRepo productRepo;
    private final Clock clock;
    private final LabelWebServiceClient labelClient;

    public ProductDto getProduct(long productId) {
        Product product = productService.getProduct(productId);
        ProductDto dto = new ProductDto(product);
        dto.sampleDate = product.getSampleDate().orElse(LocalDate.now(clock)).toString();
        return dto;
    }
    // iti face tranzactie noua, diferita de cea care vine din teste.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createProduct(ProductDto productDto) {
        // TODO
        // repo.save(e); - buba
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }

    public String getLabel(long id) {
        List<Label> labels = labelClient.retrieveAllLabels();
        return labels.stream().filter(l -> l.getId() == id).findFirst().get().getValue();
    }
}

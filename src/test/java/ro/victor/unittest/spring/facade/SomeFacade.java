package ro.victor.unittest.spring.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.victor.unittest.spring.domain.RestInPeaceService;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.Peace;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SomeFacade {
    private final RestInPeaceService domainService;
    private final ProductRepo productRepo;


    public Peace getPeace(String ssn) {
        return domainService.getPeace(ssn);
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }
}

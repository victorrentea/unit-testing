package victor.testing.spring.repo;

import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

public interface ProductRepoSearch {
    List<ProductSearchResult> search(ProductSearchCriteria criteria);


//    @Query("SELECT p FROM Product p JOIN p.bla WHERE EIXST")
}

package victor.testing.spring.repo;

import victor.testing.spring.api.dto.ProductSearchCriteria;
import victor.testing.spring.api.dto.ProductSearchResult;

import java.util.List;

public interface ProductRepoSearch {
    List<ProductSearchResult> search(ProductSearchCriteria criteria);
}

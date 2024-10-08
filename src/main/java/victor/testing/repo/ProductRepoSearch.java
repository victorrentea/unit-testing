package victor.testing.repo;

import victor.testing.api.dto.ProductSearchCriteria;
import victor.testing.api.dto.ProductSearchResult;

import java.util.List;

public interface ProductRepoSearch {
    List<ProductSearchResult> search(ProductSearchCriteria criteria);
}

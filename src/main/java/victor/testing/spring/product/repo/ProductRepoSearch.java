package victor.testing.spring.product.repo;

import victor.testing.spring.product.api.dto.ProductSearchCriteria;
import victor.testing.spring.product.api.dto.ProductSearchResult;

import java.util.List;

public interface ProductRepoSearch {
    List<ProductSearchResult> search(ProductSearchCriteria criteria);
}

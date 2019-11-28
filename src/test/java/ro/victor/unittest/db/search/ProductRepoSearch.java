package ro.victor.unittest.db.search;

import java.util.List;

public interface ProductRepoSearch {
    List<Product> search(ProductSearchCriteria criteria);
}

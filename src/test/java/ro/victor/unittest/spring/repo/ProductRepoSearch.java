package ro.victor.unittest.spring.repo;

import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import java.util.List;

public interface ProductRepoSearch {
    List<Product> search(ProductSearchCriteria criteria);
}

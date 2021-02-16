package victor.testing.spring.repo;

import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import java.util.List;

public interface ProductRepoSearch {
    List<Product> search(ProductSearchCriteria criteria);

    void process();
}

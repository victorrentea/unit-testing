package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.testing.spring.domain.Product;

public interface ProductRepo extends ProductRepoSearch, JpaRepository<Product, Long> {
   Product findByName(String name);

}

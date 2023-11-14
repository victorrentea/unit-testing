package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.testing.spring.domain.Product;

import java.util.List;

public interface ProductRepo extends ProductRepoSearch, JpaRepository<Product, Long> {
   @Query("SELECT p FROM Product p where p.name = ?1")
   List<Product> findByName(String name);

}

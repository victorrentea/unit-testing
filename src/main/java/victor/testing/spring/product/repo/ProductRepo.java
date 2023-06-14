package victor.testing.spring.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.testing.spring.product.domain.Product;

public interface ProductRepo extends ProductRepoSearch, JpaRepository<Product, Long> {
   @Query("SELECT p FROM Product p where p.name = ?1")
   Product findByName(String name);
//   @Query("SELECT new ProductDto(p.id, p.name) FROM Product p where p.name = ?1")
//   Product findByIdDto(String name);

}

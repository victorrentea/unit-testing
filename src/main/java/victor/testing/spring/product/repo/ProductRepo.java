package victor.testing.spring.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import victor.testing.spring.product.domain.Product;

public interface ProductRepo extends ProductRepoSearch, JpaRepository<Product, Long> {
//   @Query("SELECT p FROM Product p where p.name = :name")
//   Product findByName(@Param("name") String name); // foloseste-o pt multi parametrii

   @Query("SELECT p FROM Product p where p.name = ?1")
   Product findByName(String name);

}

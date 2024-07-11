package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.testing.spring.domain.Product;

public interface ProductRepo extends ProductRepoSearch, JpaRepository<Product, Long> {
   @Query("SELECT p FROM Product p where p.name = ?1")
   Product findByName(String name);

   // i get to test native SQL queries
   @Query(nativeQuery = true,
       value = "SELECT count(1) FROM PRODUCT p where p.name = ?1")
   Integer countByName(String name);



}

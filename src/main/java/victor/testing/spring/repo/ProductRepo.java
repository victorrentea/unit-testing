package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.testing.spring.entity.Product;

public interface ProductRepo extends ProductRepoSearch, JpaRepository<Product, Long> {
  @Query("SELECT p FROM Product p where p.name = ?1")
  Product findByName(String name);

  // TODO introduce a bug in the query
  @Query(value = "select count(1) from PRODUCT where name = ?1", nativeQuery = true)
  int countByName(String code);
}

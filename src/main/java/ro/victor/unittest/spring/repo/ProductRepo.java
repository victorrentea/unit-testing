package ro.victor.unittest.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.victor.unittest.spring.domain.Product;

import java.time.LocalDateTime;

public interface ProductRepo extends ProductRepoSearch, JpaRepository<Product, Long> {
   @Query("SELECT p FROM Product p where p.name = ?1")
   Product findByName(String name);

   Product findByCreateDateBetween(LocalDateTime start, LocalDateTime end);
}

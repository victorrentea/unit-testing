package victor.testing.design.purity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.testing.design.purity.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {
   @Query("SELECT p FROM Product p where p.name = ?1")
   Product findByName(String name);

}

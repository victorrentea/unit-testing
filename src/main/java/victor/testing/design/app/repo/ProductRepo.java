package victor.testing.design.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.testing.design.app.domain.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

}

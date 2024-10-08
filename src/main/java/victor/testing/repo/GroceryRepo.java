package victor.testing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.testing.entity.Grocery;

import java.util.Optional;

public interface GroceryRepo extends JpaRepository<Grocery, String> {
    Optional<Grocery> findByName(String name);
}

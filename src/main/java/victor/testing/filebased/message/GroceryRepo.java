package victor.testing.filebased.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroceryRepo extends JpaRepository<Grocery, String> {
    Optional<Grocery> findByName(String name);
}

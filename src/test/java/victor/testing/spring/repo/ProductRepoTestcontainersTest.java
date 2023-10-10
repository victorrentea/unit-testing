package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.api.dto.ProductSearchCriteria;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
public class ProductRepoTestcontainersTest extends IntegrationTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @BeforeEach
    public void detectIncomingDataLeaks() {
        // good idea for larger projects
        assertThat(repo.count()).isEqualTo(0);
    }

    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }
}


package victor.testing.spring.product.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseIntegrationTest;
import victor.testing.spring.product.api.dto.ProductSearchCriteria;
import victor.testing.spring.product.domain.Product;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
public class ProductRepoTestcontainersTest extends BaseIntegrationTest {
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


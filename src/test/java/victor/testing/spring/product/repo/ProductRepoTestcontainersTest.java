package victor.testing.spring.product.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseDatabaseTest;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.api.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
public class ProductRepoTestcontainersTest extends BaseDatabaseTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @BeforeEach
    public void initialize() {
        assertThat(repo.count()).isEqualTo(0); // good idea for larger projects
    }

    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }
}


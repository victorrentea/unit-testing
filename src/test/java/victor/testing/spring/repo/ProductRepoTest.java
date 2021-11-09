package victor.testing.spring.repo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem") // remove me
public class ProductRepoTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Test
    public void noCriteria() {
        repo.save(new Product("A"));
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    @Disabled("TODO enable")
    public void noCriteriaBis() {
        repo.save(new Product("B"));
        assertThat(repo.search(criteria)).hasSize(1);
    }

}


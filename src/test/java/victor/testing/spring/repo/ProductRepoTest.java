package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
public class ProductRepoTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ProductRepoTest.class);

    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @BeforeEach
    final void before() {
        repo.deleteAll(); // solutie foarte ok pt a curata orice resurse ramane murdare
        // (mai putin baze relationale , pt care avem o solutie mult mai buna)
    }
    @Test
    public void noCriteria() throws InterruptedException {
        repo.save(new Product("A"));
        List<ProductSearchResult> results = repo.search(criteria);
        assertThat(results).hasSize(1);
    }
    @Test
    public void noCriteriaBis() {
        repo.save(new Product("B"));
        List<ProductSearchResult> results = repo.search(criteria);
        assertThat(results).hasSize(1);
    }

}


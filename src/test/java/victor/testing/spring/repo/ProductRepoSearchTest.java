package victor.testing.spring.repo;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
// Daca esti pe Junit4 mai trebuie @RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("db-real")
@Transactional
public class ProductRepoSearchTest {
    private static final Logger log = LoggerFactory.getLogger(ProductRepoSearchTest.class);
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @BeforeEach
    public void initialize() {
//        repo.deleteAll();
    }
    @Test
    public void noCriteria() {
        repo.save(new Product().setName("A"));
        Assert.assertEquals(1, repo.search(criteria).size());
    }
    @Test
    public void noCriteriaCa() {
        repo.process();
    }

    @Test
    public void noCriteriaBis() {
        repo.save(new Product().setName("B")
            .setSupplier(new Supplier()));

        List<Product> results = repo.search(criteria);

        assertThat(results).hasSize(1);

    }

    // TODO finish

    // TODO base test class persisting supplier

    // TODO replace with composition
}


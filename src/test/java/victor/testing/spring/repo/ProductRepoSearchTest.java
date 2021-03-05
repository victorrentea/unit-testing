package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Test
    public void noCriteria() {
        repo.save(new Product("Copac"));
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void byName() {
        repo.save(new Product("Pom"));
        criteria.name="O";
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.name="x";
        assertThat(repo.search(criteria)).isEmpty();
    }
//    @Test
//    public void bySupplier() {
//        repo.save(new Product("Pom"));
//        assertThat(repo.search(criteria)).hasSize(1);
//    }

    // TODO finish

    // TODO base test class persisting supplier

    // TODO replace with composition
}


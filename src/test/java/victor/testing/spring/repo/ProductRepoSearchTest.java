package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
//@ClearAllTables
@Transactional
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void byName() {
        repo.save(new Product("GirAfa"));
        criteria.name="Girafa";
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.name="Caine";
        assertThat(repo.search(criteria)).isEmpty();
        criteria.name="girafa";
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.name="iRa";
        assertThat(repo.search(criteria)).hasSize(1);
    }

    // TODO base test class persisting supplier

    // TODO replace with composition
}


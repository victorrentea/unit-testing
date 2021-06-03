package victor.testing.spring.repo;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductRepoTest extends BaseRepoTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Test
    public void noCriteria() {
        repo.save(new Product("A"));
        Assertions.assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
    @Test
    public void noCriteriaBis() {
        repo.save(new Product("B"));
        Assertions.assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
}



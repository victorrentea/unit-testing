package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"db-sybase", "insertDummyData"})
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Test
    @Transactional
    public void noCriteria() {
        repo.save(new Product());

        int alLCount = (int) repo.count();

        List<ProductSearchResult> results = repo.search(criteria);

        assertThat(results).hasSize(alLCount + 1);
    }

    @Test
    @Transactional
    public void byName() {
        String r = UUID.randomUUID().toString();
        repo.save(new Product("tree"+r));

        criteria.name = "tree"+r;

        List<ProductSearchResult> results = repo.search(criteria);

        assertThat(results).hasSize(1);
    }
    @AfterTransaction
    public void deleteRing() {
        jdbc.update("DELETE FROM PRODUCT WHERE name=?", "You wedding ring");
    }


    @Autowired
    private JdbcTemplate jdbc;


    // TODO finish

    // TODO base test class persisting supplier

    // TODO replace with composition
}


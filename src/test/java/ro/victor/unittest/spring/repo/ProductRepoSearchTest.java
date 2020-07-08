package ro.victor.unittest.spring.repo;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
@ActiveProfiles("db-real")
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    public ProductRepoSearchTest() {
        System.out.println("New test class instance");
    }

    @Before
    public void deleteAll() {
//        repo.deleteAll();
        assertThat(repo.count()).isEqualTo(0); //precheck
    }
    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
//    @Commit // doar pt debug
    public void byName() {
        criteria.name = "x";
        repo.save(new Product("aXb"));
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.name = "B";
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.name = "A";
        assertThat(repo.search(criteria)).hasSize(1);
    }

    // TODO
}


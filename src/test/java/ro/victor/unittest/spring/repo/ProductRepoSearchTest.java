package ro.victor.unittest.spring.repo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("db-mem")
@Transactional
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    public ProductRepoSearchTest() {
        System.out.println("New test class instance");
    }

    @Before
    public void initialize() {
//        repo.deleteAll();
        assertThat(repo.count()).isEqualTo(0);
    }
    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void byName() {
        repo.save(new Product().setName("Tree"));
        criteria.name = "R";
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.name = "x";
        assertThat(repo.search(criteria)).hasSize(0);
    }
}


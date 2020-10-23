package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("db-mem")
@Transactional
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Before
    public void cleanDatabase() {
    	// delete din tabele car e te ForeignKeyaza pe tina
//    	repo.deleteAll();
    }
    
    @Test
    public void noCriteria() {
    	repo.save(new Product("A"));
    	Assert.assertEquals(1, repo.search(criteria).size());
    	Assertions.assertThat(repo.search(criteria)).hasSize(1);
    }
    
    @Test
    public void noCriteriaBis() {
        repo.save(new Product("B"));
//        Assert.assertEquals(1, repo.search(criteria).size());
        Assertions.assertThat(repo.search(criteria)).hasSize(1);
    }

    // TODO finish

    // TODO base test class persisting supplier

    // TODO replace with composition
}


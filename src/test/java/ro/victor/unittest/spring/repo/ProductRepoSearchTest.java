package ro.victor.unittest.spring.repo;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"db-mysql", "test"})
@Category(IntegrationTest.class)
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    public ProductRepoSearchTest() {
        System.out.println("New test class instance");
    }

    @After
    public void mamaMiaSpusSaCuratDupaMine() {
        repo.deleteAll();
    }

    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void noCriteria2() {
        repo.save(new Product());
//        Assert.assertTrue(1 == repo.search(criteria).size());
//        Assert.assertEquals(1, repo.search(criteria).size());
        assertThat(repo.search(criteria)).hasSize(1); // better failure message - to string la colectie.
    }

    // TODO
}


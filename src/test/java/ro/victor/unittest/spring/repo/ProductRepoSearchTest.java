package ro.victor.unittest.spring.repo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.WaitForDBInitializer;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.ProductService;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"db-mem", "test"})
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // brutal, don't do it
//@Category(IntegrationTest.class)
//@ContextConfiguration(initializers = WaitForDBInitializer.class)
@Transactional
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();
    @Autowired
    private ProductService productService;

    @Before
    public void initialize() {
        repo.deleteAll();// probleme pt ca tre sa stergi din tabele intr-o anumita ordine.
        // (Dupa FK)
        // Singura solutie daca lucrezi fara spring.
        // .. sau daca codul testat foloseste REQUIRES_NEW si face noi
        // tranzactii pe care le comit esingur, independent de tranzactia de test!
    }

    @Test
    public void noCriteria() {
        //start TX
        productService.saveProduct(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
        // ROLLBACK
    }
    @Test
    public void noCriteria2() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    // TODO continuat de testat TOT SEARCH CRITERIA
}


package ro.victor.unittest.spring.repo;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"db-real", "test"})
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) // rade tot dar costa timp.
@Transactional

public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    public ProductRepoSearchTest() {

        System.out.println("New test class instance");
    }


//    @After
//    public void mamaMiaSpusSaCuratDupaMine() {
//        repo.deleteAll();
//    }

    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
//    @Commit
    public void byName() {
        repo.save(new Product("Paine"));
        criteria.name = "N";
        assertThat(repo.search(criteria)).hasSize(1); // better failure message - to string la colectie.
    }
    @Test
//    @Commit
    public void byNameMismatch() {
        repo.save(new Product("Paine"));
        criteria.name = "XNOTINDB";
        assertThat(repo.search(criteria)).isEmpty(); // better failure message - to string la colectie.
    }

    // TODO
}


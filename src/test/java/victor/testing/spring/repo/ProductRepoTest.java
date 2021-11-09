package victor.testing.spring.repo;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

@Slf4j
@SpringBootTest
@ActiveProfiles("db-mem")
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // distruge contextul de spring: rupe jenkinsul in doua. NU COMITI Pe GIT ci folosesti doar pentru a investiga cuplari prin spring:
// exemple
// 1) cache
// 2) DB
// 3) state pe singletoane
// 4) register la cozi


// doar ora are CONNECT BY,   /*+ hint */

@Transactional

//@Sql(scripts = "classpath:cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ProductRepoTest {
    @Autowired
    private ProductRepo repo;
    @Autowired
    private SupplierRepo supplierRepo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    // anti pattern in DB relational: se fol doar pt resurse non-tranzactionate: fisiere, nosql DBs, cache-uri
    //    @BeforeEach
//    final void before() {
//        log.info("Clean db");
//        supplierRepo.deleteAll();
//        repo.deleteAll();
//    }
    @Test
    public void noCriteria() {
        repo.save(new Product("A"));
        Assertions.assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void noCriteriaBis() {
        repo.save(new Product("B"));
        Assertions.assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void asdsabySupplier() {
        repo.save(new Product("B")).setSupplier(new Supplier());
        Assertions.assertThat(repo.search(criteria)).hasSize(1);
    }

}


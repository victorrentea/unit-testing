package victor.testing.spring.repo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@ActiveProfiles("db-mem")
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // - NU are voie sa ajunga comisa pe GIT. O folosesti doar pe local sa faci de bugging: adica: cu @DC mai crapa testul asta rulat cu fratii ? daca NU -> leaking state a fost legata de spring: 95% - ceva ramas in DB din mem 5% state prin singleton sau cacheuri

@Transactional // >>>> RUPE ! 99% din testele relationale trebuie scrise asa
public class ProductRepoTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ProductRepoTest.class);

    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

//    @BeforeEach
//    final void before() {
//        repo.deleteAll(); // solutie foarte ok pt a curata orice resurse ramane murdare
//        // (mai putin baze relationale , pt care avem o solutie mult mai buna)
//    }
    @Test
    // @Commit -- il pui doar ca sa vezi ceva date comituite intr-o
    // baza fizica cand faci debug
    public void noCriteria() throws InterruptedException {
        repo.save(new Product("A"));
        List<ProductSearchResult> results = repo.search(criteria);
        assertThat(results).hasSize(1);
    }
    @Test
    public void noCriteriaBis() {
        repo.save(new Product("B"));
        List<ProductSearchResult> results = repo.search(criteria);
        assertThat(results).hasSize(1);
    }

}


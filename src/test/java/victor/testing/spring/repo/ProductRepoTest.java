package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

@SpringBootTest
@Transactional
@ActiveProfiles("db-mem")
//@Testcontainers
public class ProductRepoTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();


//    @BeforeEach
//    final void before() { // doar daca ai REQUIRES_NEW/NOT_SUPPORTED in codul testat altfel NU
//        repo.deleteAll();
//    }

    @Autowired
    ServiciuCareAreTranzactieSeparata serviciuCareAreTranzactieSeparata;

    @Test
    public void noCriteria() {
//        serviciuCareAreTranzactieSeparata.method(); // crapa daca dcomentezi
        repo.save(new Product("A"));
        Assertions.assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
    @Test
    public void noCriteriaBis() {
        repo.save(new Product("B"));
        Assertions.assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
    @Test
    public void matchByName() {
        repo.save(new Product("A"));
        repo.save(new Product("B"));
        criteria.name="B";
        Assertions.assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
//    @Test
//    public void noMatchByName() {
//        repo.save(new Product("B"));
//        criteria.name="A";
//        Assertions.assertThat(repo.search(criteria)).isEmpty(); // org.assertj:assertj-core:3.16.1
//    }

}


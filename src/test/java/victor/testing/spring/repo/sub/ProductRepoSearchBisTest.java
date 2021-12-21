package victor.testing.spring.repo.sub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("db-mem")
@SpringBootTest
@Transactional
public class ProductRepoSearchBisTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @BeforeEach
    final void before() {
//        assertThat(repo.findAll()).isEmpty(); // pre assumption < el criticon
        repo.deleteAll(); // shoud be useless unless others leave garbage behind
    }
    @Test
    public void noCriteria() throws InterruptedException {
        repo.save(new Product("A"));

        List<ProductSearchResult> results = repo.search(criteria);

        assertThat(results).hasSize(1);
    }
    @Test
    public void byNameNotMatch() {
        repo.save(new Product("aBc"));

        criteria.name = "B";
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void byNameMatch() {
        repo.save(new Product("B"));

        criteria.name = "C";
        assertThat(repo.search(criteria)).isEmpty();
    }
}

//@Slf4j
//@RequiredArgsConstructor
//@Service
//class MyService {
//    private ProductRepo productRepo;
//
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void method() {
//        productRepo
//    }
//}

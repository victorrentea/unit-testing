package victor.testing.spring.repo.packa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.RepoTestBase;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;


//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // never push this on Git

@Transactional
public class ProductRepoTest extends RepoTestBase {
    @Autowired
    private ProductRepo repo;
    @Autowired
    SupplierRepo supplierRepo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    // good idea
//    @BeforeEach
//    final void before() {
//        supplierRepo.deleteAll();
//        repo.deleteAll(); // the best option for any non-transacted systems
//    }
    @BeforeEach
    final void before() {
        supplierRepo.save(new Supplier());

    }

    @Test
    public void noCriteria() {
        repo.save(new Product("A"));

        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void byName() {
        repo.save(new Product("name"));

        criteria.name = "name";
        assertThat(repo.search(criteria)).hasSize(1);

        criteria.name = "FOUND";
        assertThat(repo.search(criteria)).hasSize(0);
    }

}


package ro.victor.unittest.spring.repo;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static ro.victor.unittest.spring.domain.Product.*;

@Slf4j
//@Rollback(false)

// profile, mockbean, props
@ActiveProfiles("test") // override props using application-test.properties
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductRepoSearchTest extends RepoBaseTest{


    // TODO DELETE all below
    // TODO search for no criteria
    // TODO search by name
    // TODO search by category
    // TODO @Before check no garbage in
    @Autowired
    private ProductRepo repo;
    @Autowired
    private SupplierRepo supplierRepo;

    private ProductSearchCriteria criteria;

    @Before/*Method*/
    public void initialize() {
        criteria = new ProductSearchCriteria();
        assertThat(repo.count()).isEqualTo(0); // verifici starea bazei la inceput
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void ano11CriteriaReturnsEverything() { // Ludovic XIV: Dupa mine, potopul
        repo.save(new Product());
        // Tranzactia de test creata de @Transactional vine de la inceput cu
        // flagul de commit pe false (adica vrea sa faca rollback)
        TestTransaction.flagForCommit();// suprascrie acel flag
        TestTransaction.end();
        TestTransaction.start();
        List<ProductSearchResult> results = repo.search(criteria);
        assertThat(results).hasSize(1);
    }
    @Test
//    @Commit sau @Rollback(false)
    public void searchByName() {
        criteria.name = "X";
        Product product = new Product().setName("axB");
        repo.save(product);
        List<ProductSearchResult> results = repo.search(criteria);
        // forma1
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(product.getId()); // optional
        // forma2, mai geeky dar mai greu de urmarit
        assertThat(results.stream().map(ProductSearchResult::getId)
                .collect(toList())).isEqualTo(asList(product.getId()));
    }
    @Test
    public void searchByNameMismatch() {
        criteria.name = "X";
        Product product = new Product().setName("Y");
        repo.save(product);
        List<ProductSearchResult> results = repo.search(criteria);
        assertThat(results).isEmpty();
    }
    @Test
    public void searchByCategory() {
        Product product = new Product()
                .setCategory(Category.PT_MINE);
        repo.save(product);

        criteria.category = Category.PT_MINE;
        assertThat(repo.search(criteria)).hasSize(1);

        criteria.category = Category.PT_NEVASTA;
        assertThat(repo.search(criteria)).isEmpty();
    }
}


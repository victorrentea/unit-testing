package ro.victor.unittest.spring.repo;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
// profile, mockbean, props
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

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Before
    public void clearCriteria() {
        assertThat(supplierRepo.count()).isEqualTo(1);
        assertThat(repo.count()).isEqualTo(0);
        log.info("BeforeMethod in test class");
    }
    @Test
    public void name() {
        Product product = new Product().setName("Lego");
        repo.save(product);
        criteria.name = "E";
        assertThat(repo.search(criteria)).anyMatch(pp -> pp.id.equals(product.getId()));
        criteria.name = "Lego";
        assertThat(repo.search(criteria)).anyMatch(pp -> pp.id.equals(product.getId()));
        criteria.name = "LegoX";
        assertThat(repo.search(criteria)).isEmpty();
    }
    @Test
    public void category() {
        Product pInDB = new Product().setCategory(Product.Category.PT_CASA);
        repo.save(pInDB);
        criteria.category = Product.Category.PT_CASA;
        assertThat(repo.search(criteria)).anyMatch(
                pp -> pp.id.equals(pInDB.getId()));
        criteria.category = Product.Category.PT_MINE;
        assertThat(repo.search(criteria)).isEmpty();
    }
}


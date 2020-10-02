package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Product.Category;
import ro.victor.unittest.spring.domain.ProductService;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"db-mem", "test"})
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // brutal, don't do it
//@Category(IntegrationTest.class)
//@ContextConfiguration(initializers = WaitForDBInitializer.class)
@Transactional
public class ProductRepoSearchTest extends AbstractRepoTestBase {
    @Autowired
    private ProductRepo productRepo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();
    @Autowired
    private ProductService productService;



    @Before
    public void initialize() {
        //repo.deleteAll();// probleme pt ca tre sa stergi din tabele intr-o anumita ordine.
        // (Dupa FK)
        // Singura solutie daca lucrezi fara spring.
        // .. sau daca codul testat foloseste REQUIRES_NEW si face noi
        // tranzactii pe care le comit esingur, independent de tranzactia de test!
    }
    @Test
    public void noCriteria() {
        //start TX
        productService.saveProduct(new Product());
        assertThat(productRepo.search(criteria)).hasSize(1);
        // ROLLBACK
    }
    @Test
    public void byName() {
        productRepo.save(new Product().setName("aB"));

        criteria.name = "Ab";
        assertThat(productRepo.search(criteria)).hasSize(1);

        criteria.name = "x";
        assertThat(productRepo.search(criteria)).isEmpty();
    }

    @Test
    public void byCategory() {
        productRepo.save(new Product().setCategory(Category.HOME));

        criteria.category = Category.HOME;
        assertThat(productRepo.search(criteria)).hasSize(1);

        criteria.category = Category.ME;
        assertThat(productRepo.search(criteria)).isEmpty();
    }
    @Test
    public void bySupplier() {
        productRepo.save(new Product().setSupplier(supplier));

        criteria.supplierId = supplier.getId();
        assertThat(productRepo.search(criteria)).hasSize(1);

        criteria.supplierId = -1L;
        assertThat(productRepo.search(criteria)).isEmpty();
    }
    @Test
    public void bySupplier2() {
        productRepo.save(new Product().setSupplier(supplier));

        criteria.supplierId = supplier.getId();
        assertThat(productRepo.search(criteria)).hasSize(1);

        criteria.supplierId = -1L;
        assertThat(productRepo.search(criteria)).isEmpty();
    }

    // TODO continuat de testat TOT SEARCH CRITERIA
}


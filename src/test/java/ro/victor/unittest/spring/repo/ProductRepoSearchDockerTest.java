package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.WaitForDBInitializer;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Product.Category;
import ro.victor.unittest.spring.domain.ProductService;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"db-mysql", "test"})

@SpringBootTest
@Transactional
@org.junit.experimental.categories.Category(IntegrationTest.class)
//@ContextConfiguration(initializers = WaitForDBInitializer.class)
public class ProductRepoSearchDockerTest {
    @Autowired
    private ProductRepo productRepo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierRepo supplierRepo;
    protected Supplier supplier;

    @BeforeEach
    public final void initialize() {

        supplier = supplierRepo.save(new Supplier().setName("IKEA"));
        // dupa, the supplier entity was assigned an ID from the DB sequence
    }


    @BeforeEach
    public void checkTheresNoProductInDB() {
        CacheManager cm;
//        cm.getCache("").cl
        // in large projects: pre-assertion ca tabela ta e goala la INEPUTUL testului ( ca cine stie ce coleg...)
        assertThat(productRepo.findAll()).isEmpty();

        //repo.deleteAll();// probleme pt ca tre sa stergi din tabele intr-o anumita ordine.
        // (Dupa FK)
        // Singura solutie daca lucrezi fara spring.
        // .. sau daca codul testat foloseste REQUIRES_NEW si face noi
        // tranzactii pe care le comit esingur, independent de tranzactia de test!
    }
    @org.junit.jupiter.api.Test
    public void noCriteria() {
        //start TX
        productService.saveProduct(new Product());
        assertThat(productRepo.search(criteria)).hasSize(1);
        // ROLLBACK
    }
    @org.junit.jupiter.api.Test
    public void byName() {
        productRepo.save(new Product().setName("aB"));

        criteria.name = "Ab";
        assertThat(productRepo.search(criteria)).hasSize(1);

        criteria.name = "x";
        assertThat(productRepo.search(criteria)).isEmpty();
    }

    @org.junit.jupiter.api.Test
    public void byCategory() {
        productRepo.save(new Product().setCategory(Category.HOME));

        criteria.category = Category.HOME;
        assertThat(productRepo.search(criteria)).hasSize(1);

        criteria.category = Category.ME;
        assertThat(productRepo.search(criteria)).isEmpty();
    }
    @org.junit.jupiter.api.Test
    public void bySupplier() {
        productRepo.save(new Product().setSupplier(supplier));

        criteria.supplierId = supplier.getId();
        assertThat(productRepo.search(criteria)).hasSize(1);

        criteria.supplierId = -1L;
        assertThat(productRepo.search(criteria)).isEmpty();
    }
    @Test
    public void bySupplier2() {
//     cu mock:   when(repo.findById(id)).theReturn(ceva);

//      fara mock:  repo.save(ceva); pe o in-mem db


        productRepo.save(new Product().setSupplier(supplier));

        criteria.supplierId = supplier.getId();
        assertThat(productRepo.search(criteria)).hasSize(1);

        criteria.supplierId = -1L;
        assertThat(productRepo.search(criteria)).isEmpty();
    }

    // TODO continuat de testat TOT SEARCH CRITERIA
}


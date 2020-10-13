package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
//@ClearAllTables
@Transactional
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;
    @Autowired
    private SupplierRepo supplierRepo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();
    private Supplier supplier;

    @BeforeEach
    public void initialize() {
        supplier = supplierRepo.save(new Supplier("IKEA"));
    }
    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void byName() {
        repo.save(new Product("GirAfa"));
        criteria.name="Girafa";
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.name="Caine";
        assertThat(repo.search(criteria)).isEmpty();
        criteria.name="girafa";
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.name="iRa";
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void byCategory() {
        repo.save(new Product().setCategory(ProductCategory.WIFE));
        criteria.category = ProductCategory.WIFE;
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.category = ProductCategory.ME;
        assertThat(repo.search(criteria)).isEmpty();
    }
    @Test
    public void bySupplier() {
        repo.save(new Product().setSupplier(supplier));
        criteria.supplierId = supplier.getId();
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.supplierId = -1L;
        assertThat(repo.search(criteria)).isEmpty();
    }

    // TODO base test class persisting supplier

    // TODO replace with composition
}


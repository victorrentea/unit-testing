package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;
    @Autowired
    private SupplierRepo supplierRepo;

    private ProductSearchCriteria criteria;
    private Supplier supplier;

    @BeforeEach
    public void initialize() {
        criteria = new ProductSearchCriteria();
        supplier = supplierRepo.save(new Supplier());
    }

    @Test
    public void noCriteria() {
        repo.save(new Product());
        Assert.assertEquals(1, repo.search(criteria).size());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    // acceptance test (15-20%) - blackbox; testul padurii
    @Test
    public void byNameCuMulteDate() {
        repo.save(new Product("Copac"));
        repo.save(new Product("Planta"));
        criteria.name = "Copac";
        assertThat(repo.search(criteria)).hasSize(1);
        assertThat(repo.search(criteria).get(0).getName()).isEqualTo("Copac");
    }
    // fine grained unit tests; teste de copac.
    @Test
    public void byName() {
        repo.save(new Product("Copac"));
        criteria.name = "Copac";
        assertThat(repo.search(criteria)).hasSize(1);
//    }
//    @Test
//    public void byNameMismatch() {
//        repo.save(new Product("Copac"));
        criteria.name = "Planta";
        assertThat(repo.search(criteria)).isEmpty();
    }

    // TODO finish

    // TODO base test class persisting supplier

    // TODO replace with composition
}


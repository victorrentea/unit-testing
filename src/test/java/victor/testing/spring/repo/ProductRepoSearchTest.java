package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRepoSearchTest extends RepoTestBase {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Test
    public void prostgresspecific() {
        repo.nativeq();
    }
    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void byName() {
        repo.save(new Product("Pom"));
        criteria.name="O";
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.name="x";
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
    @Test
    public void bySupplierBis() {
        repo.save(new Product().setSupplier(supplier));
        criteria.supplierId = supplier.getId();
        assertThat(repo.search(criteria)).hasSize(1);
        criteria.supplierId = -1L;
        assertThat(repo.search(criteria)).isEmpty();
    }

    // TODO finish

    // TODO base test class persisting supplier

    // TODO replace with composition
}


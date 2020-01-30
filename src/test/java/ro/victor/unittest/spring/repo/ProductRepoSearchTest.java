package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

// profile, mockbean, props
public class ProductRepoSearchTest extends RepoBaseTest{

    // TODO DELETE all below
    // TODO search for no criteria
    // TODO search by name
    // TODO search by category
    // TODO @Before check no garbage in

    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Before
    public void checkNoRowsInDbInitially() {
        assertEquals(0, repo.count());
    }
    @Test
    public void noCriteria() {
        Product productInDb = new Product();
        repo.save(productInDb);
        List<Product> products = repo.search(criteria);
        assertEquals(asList(productInDb.getId()),
                products.stream().map(Product::getId).collect(toList()));
    }

    @Test
    public void byName() {
        repo.save(new Product().setName("name"));
        criteria.name = "aM";
        assertThat(repo.search(criteria)).hasSize(1);

        criteria.name = "cucu";
        assertThat(repo.search(criteria)).hasSize(0);
    }

    @Test
    public void byCategory() {
        repo.save(new Product().setCategory(Product.Category.COPII));
        criteria.category = Product.Category.COPII;
        assertThat(repo.search(criteria)).hasSize(1);

        criteria.category = Product.Category.NEVASTA;
        assertThat(repo.search(criteria)).hasSize(0);
    }
}


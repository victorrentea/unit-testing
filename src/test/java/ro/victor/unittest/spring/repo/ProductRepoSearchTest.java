package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.infra.ExternalServiceClient;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

// profile, mockbean, props
@TestPropertySource(properties = {"current.date=2019-01-30"})
@ActiveProfiles("test")
public class ProductRepoSearchTest extends RepoBaseTest{

    // TODO DELETE all below
    // TODO search for no criteria
    // TODO search by name
    // TODO search by category
    // TODO @Before check no garbage in

    @MockBean
    private ExternalServiceClient mockClient;

    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();
    private Product product = new Product();

    @Before
    public void checkNoRowsInDbInitially() {
        product.setCreationDate(LocalDate.now());
        assertEquals(0, repo.count());
        Mockito.when(mockClient.callService()).thenReturn("Call me Baby!");
    }
    @Test
    public void noCriteria() {
        repo.save(product);
        List<Product> products = repo.search(criteria);
        assertEquals(asList(product.getId()),
                products.stream().map(Product::getId).collect(toList()));
    }

    @Test
    public void byName() {
        repo.save(product.setName("name"));
        criteria.name = "aM";
        assertThat(repo.search(criteria)).hasSize(1);

        criteria.name = "cucu";
        assertThat(repo.search(criteria)).hasSize(0);
    }

    @Test
    public void byCategory() {
        repo.save(product.setCategory(Product.Category.COPII));
        criteria.category = Product.Category.COPII;
        assertThat(repo.search(criteria)).hasSize(1);

        criteria.category = Product.Category.NEVASTA;
        assertThat(repo.search(criteria)).hasSize(0);
    }

    @Test
    public void byCreationDate() {
        repo.save(product.setCreationDate(LocalDate.now().minusDays(40)));
        assertThat(repo.search(criteria)).hasSize(0);

        //

//        assertThat(repo.search(criteria)).hasSize(1);
    }
}


package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.infra.ExternalServiceClient;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

// profile, mockbean, props

public class ProductRepoSearchTest extends RepoBaseTest{

    @Autowired
    private ProductRepo repo;

    @Autowired
    private EntityManager em;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @MockBean
    public ExternalServiceClient externalServiceClient;


    @Before
    public void verificCaECuratInBaza() {
        assertThat(repo.count()).isEqualTo(0);
    }
    @Test
    public void name() {

        Mockito.when(externalServiceClient.callService()).thenReturn("P'asta");
        Product pInDB = new Product().setName("Lego").setOriginCountry(c);
        repo.save(pInDB);
        criteria.name = "E";
        assertThat(repo.search(criteria)).anyMatch(pp -> pp.getId().equals(pInDB.getId()));
        criteria.name = "Lego";
        assertThat(repo.search(criteria)).anyMatch(pp -> pp.getId().equals(pInDB.getId()));
        criteria.name = "LegoX";
        assertThat(repo.search(criteria)).isEmpty();
    }
    @Test
    public void category() {
        Product pInDB = new Product().setCategory(Product.Category.NEVASTA);
        repo.save(pInDB);
        criteria.category = Product.Category.NEVASTA;
        assertThat(repo.search(criteria)).anyMatch(
                pp -> pp.getId().equals(pInDB.getId()));
        criteria.category = Product.Category.COPII;
        assertThat(repo.search(criteria)).isEmpty();
    }
}


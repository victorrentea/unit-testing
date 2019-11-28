package ro.victor.unittest.db.search;

import lombok.ToString;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.db.search.Product.Category;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// profile, mockbean, props

public class ProductSearchTest extends RepoBaseTest{

    @Autowired
    private ProductRepo repo;

    @Autowired
    private EntityManager em;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Before
    public void verificCaECuratInBaza() {
        assertThat(repo.count()).isEqualTo(0);
    }
    @Test
    public void name() {

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
        Product pInDB = new Product().setCategory(Category.NEVASTA);
        repo.save(pInDB);
        criteria.category = Category.NEVASTA;
        assertThat(repo.search(criteria)).anyMatch(
                pp -> pp.getId().equals(pInDB.getId()));
        criteria.category = Category.COPII;
        assertThat(repo.search(criteria)).isEmpty();
    }
}

class ProductSearchCriteria { // pute a JSON
    public String name;
    public Category category;
}

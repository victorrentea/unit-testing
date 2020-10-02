package victor.testing.spring.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import victor.testing.spring.domain.Product;
import victor.testing.spring.facade.ProductSearchCriteria;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@RunWith(SpringRunner.class)
@ActiveProfiles({"db-mem", "test"})
//@Category(IntegrationTest.class)
public class ProductRepoSearch5Test {
    @Autowired
    private ProductRepo repo;

    @Autowired
    private EntityManager em;
    @RegisterExtension
    public CommonDataExtension extension = new CommonDataExtension();

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    // TODO
}


class CommonDataExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        EntityManager em = SpringExtension.getApplicationContext(context).getBean(EntityManager.class);
        System.out.println("******** Insert Common data via entity manager: " + em);
    }
}

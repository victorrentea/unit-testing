package ro.victor.unittest.spring.repo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    @Autowired
    private MyProto myProto;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    public ProductRepoSearchTest() {
        System.out.println("New test class instance");
    }

    @Before
    public void m() {
        System.out.println("proto = " + myProto
        );
    }
    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
        repo.deleteAll();
    }
    @Test
    public void noCriteria2() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    // TODO
}


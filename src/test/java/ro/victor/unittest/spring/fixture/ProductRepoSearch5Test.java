package ro.victor.unittest.spring.fixture;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"db-mem", "test"})
@Transactional
public class ProductRepoSearch5Test {
    @Autowired
    private PostRepo repo;

    // Favor Composition over Inheritance
    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @RegisterExtension
    public CommonDataExtension commonData = new CommonDataExtension();
    @Test
    public void noCriteria() {
        User user = commonData.getUser();
        repo.save(new Post().setCreatedBy(user));
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    public void noCriteria2() {
        User user = commonData.getUser();
        Assert.assertNotNull(user);
    }
}

class CommonDataExtension implements BeforeEachCallback {
    private User user;

    public User getUser() {
        return user;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        EntityManager em = SpringExtension.getApplicationContext(context).getBean(EntityManager.class);
        user = new User().setUsername("test");
        em.persist(user);
    }
}

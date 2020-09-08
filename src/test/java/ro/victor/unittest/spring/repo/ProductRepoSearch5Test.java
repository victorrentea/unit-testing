package ro.victor.unittest.spring.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.EntityManagerFactoryAccessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"db-mem", "test"})
public class ProductRepoSearch5Test {
    @Autowired
    private ProductRepo repo;

    // Favor Composition over Inheritance
    @RegisterExtension
    public CommonDataExtension extension = new CommonDataExtension();

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Test
    public void noCriteria() {
        repo.save(new Product());
//        User user = extension.getUser();
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void noCriteria2() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    // TODO
}


class CommonDataExtension implements BeforeEachCallback {
//    private User user;
//
//    public User getUser() {
//        return user;
//    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        EntityManager em = SpringExtension.getApplicationContext(context).getBean(EntityManager.class);
//        user = new User().setUserName("test");
//        em.save(user);
        System.out.println("******** Insert Common data via entity manager: " + em);
    }
}

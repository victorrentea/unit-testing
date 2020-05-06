package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Supplier;

import javax.persistence.EntityManager;

// @SpringBootTest , or, even better (faster):
@DataJpaTest
@Transactional
@Rollback(false)
public class RepoBaseTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    protected EntityManager em;

    protected Supplier c = new Supplier().setName("Romanica");

    @Before
    public void insertRefCountry() {
        em.persist(c);
    }
}

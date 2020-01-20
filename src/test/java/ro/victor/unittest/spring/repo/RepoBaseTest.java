package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Country;

import javax.persistence.EntityManager;

// @SpringBootTest , or, even better (faster):
@DataJpaTest
@RunWith(SpringRunner.class)
@Transactional
public class RepoBaseTest {
    @Autowired
    protected EntityManager em;

    protected Country c = new Country().setName("Romanica");

    @Before
    public void insertRefCountry() {
        em.persist(c);
    }
}

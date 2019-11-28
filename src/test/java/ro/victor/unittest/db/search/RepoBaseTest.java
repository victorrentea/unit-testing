package ro.victor.unittest.db.search;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class RepoBaseTest {
    @Autowired
    private EntityManager em;
    protected Country c = new Country().setName("Romanica");

    @Before
    public void insertRefCountry() {
        em.persist(c);
    }
}

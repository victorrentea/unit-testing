package ro.victor.unittest.spring.repo;


import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Supplier;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@Slf4j
@DataJpaTest // faster than @SpringBootTest
@Transactional
//@Rollback(false) -- decomenteaza daca vrei sate uiti in baza ce ramane dupa test
public class RepoBaseTest {
    @Autowired
    protected EntityManager em;

    protected Supplier c  = new Supplier().setName("Romanica");

    @Before
    public void insertRefCountry() {
        log.info("Inserting ref country");
        em.persist(c);
    }
}

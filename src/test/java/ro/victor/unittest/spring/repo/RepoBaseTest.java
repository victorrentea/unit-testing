package ro.victor.unittest.spring.repo;


import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import ro.victor.unittest.spring.domain.Supplier;

import javax.persistence.EntityManager;

@Slf4j
// @SpringBootTest , or, even better (faster):
@DataJpaTest
//@Transactional
//@Rollback(false) -- decomenteaza daca vrei sate uiti in baza ce ramane dupa test
public class RepoBaseTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    protected EntityManager em;

    protected Supplier c;

    @BeforeMethod
    @Transactional
    public void insertRefCountry() {
        log.info("Inserting country");
        c = new Supplier().setName("Romanica");
        em.persist(c);
    }
}

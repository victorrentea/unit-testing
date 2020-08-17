package ro.victor.unittest.spring.repo;


import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Supplier;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
@Transactional
//@Commit //-- uncomment to let the test transaction be commited
public abstract class RepoTestBase {
    @Autowired
    protected EntityManager em;

    protected Supplier c  = new Supplier().setName("emag");

    @Before
    public void insertSupplier() {
        log.info("Inserting supplier");
        em.persist(c);
    }
}

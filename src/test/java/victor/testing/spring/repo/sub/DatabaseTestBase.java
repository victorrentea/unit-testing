package victor.testing.spring.repo.sub;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.repo.ProductRepo;

@ActiveProfiles("db-mem")
@SpringBootTest
//@DataJpaTest
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // never on git
@Transactional // TEH BEST for RElational DBs!!!
//@Sql(scripts = "/sql/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class DatabaseTestBase {
   @Autowired
   private ProductRepo repo;

   @AfterEach
   final void checkNoGarbageAfter() {
//        assertThat(repo.findAll()).isEmpty(); // pre assumption < el criticon
   }
}

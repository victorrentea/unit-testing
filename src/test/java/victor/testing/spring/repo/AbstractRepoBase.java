package victor.testing.spring.repo;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
public abstract class AbstractRepoBase {
   @BeforeEach
   public final void persistRefData() {
//        repo.deleteAll();  __ singura pt nosql
   }
}

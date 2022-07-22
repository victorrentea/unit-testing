package victor.testing.spring.service.subpachet;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
//@Sql("insert_referentials.sql")
public abstract class BaseDBInMemoryTest {
//    protected Long userId ;
    @BeforeEach
    final void before() {
//        userrepo.save(user);
//        isnertReferentials
    }
}

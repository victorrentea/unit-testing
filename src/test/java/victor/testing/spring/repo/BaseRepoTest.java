package victor.testing.spring.repo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
@Sql(value = "/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class BaseRepoTest {
}

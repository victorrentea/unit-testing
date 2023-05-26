package victor.testing.spring.service;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

// the db spring/hibernate needs can be:
// - in-mem H2
// - in a Docker just for tests (@Testcontainers ftw)
@SpringBootTest//(properties = "a=different")
@ActiveProfiles({"db-mem","wiremock"})
@Transactional // every @Test runs in its own transaction, ROLLEDBACK automatically after each test
// NOT working if 1) you use @Transactional(propagation=REQUIRES_NEW) in prod 2) you test multithreaded code 3) you do Transaction.start yoursef
//@Sql(scripts = "classpath:/sql/cleanup.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // for terrible PL/SQL database
@Sql(scripts = "classpath:/sql/common-reference-data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // for terrible PL/SQL database
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // NEVER PUSH THIS ON GIT. only use it if you develop extensions to spring
public abstract class BaseTest {
}

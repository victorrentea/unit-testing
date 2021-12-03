package victor.testing.spring.repo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface OnCleanDatabase {

}

@SpringBootTest
@ActiveProfiles("db-mem") // remove me
//@OnCleanDatabase
//@Sql(value = "/sql/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class RepoTestBase {
}

package victor.testing.spring.repo;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,
    statements = {
        "DELETE FROM PRODUCT"
    })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClearAllTables {
}

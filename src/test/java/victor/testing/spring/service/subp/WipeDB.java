package victor.testing.spring.service.subp;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Sql(value = "classpath:/sql/cleanup.sql",
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WipeDB {
}

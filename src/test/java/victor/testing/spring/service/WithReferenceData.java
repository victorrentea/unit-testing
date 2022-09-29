package victor.testing.spring.service;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Sql(value = "classpath:/sql/common-reference-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithReferenceData {
}

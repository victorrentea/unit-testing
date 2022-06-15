package victor.testing.spring.service;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Sql(scripts = "classpath:/sql/cleanup.sql")
public @interface CleanupDB {

}

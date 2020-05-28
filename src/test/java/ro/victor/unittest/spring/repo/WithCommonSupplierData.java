package ro.victor.unittest.spring.repo;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Sql("classpath:/SupplierData.sql")
public @interface WithCommonSupplierData {
}

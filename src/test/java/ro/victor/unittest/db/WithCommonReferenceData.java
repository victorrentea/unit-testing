// SOLUTION
package ro.victor.unittest.db;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Sql("/common-reference-data.sql")
public @interface WithCommonReferenceData {

}

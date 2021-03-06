// SOLUTION
package victor.testing.db;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Sql("/common-reference-data.sql")
public @interface WithCommonReferenceData {

}

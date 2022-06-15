// SOLUTION
package victor.testing.dbunit;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Sql("/sql/common-reference-data.sql")
public @interface WithCommonReferenceData {

}

package victor.testing.spring.repo;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/product-search.feature",
        glue = {"victor.testing.spring.repo","victor.testing.tools"})
public class ProductSearchFeature {

//   @ClassRule
//   public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
//       .withDatabaseName("prop")
//       .withUsername("postgres")
//       .withPassword("password");
}

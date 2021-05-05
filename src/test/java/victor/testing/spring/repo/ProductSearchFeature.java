package victor.testing.spring.repo;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

// Why still on JUnit4: https://github.com/cucumber/cucumber-jvm/issues/1149#issuecomment-611716745
@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:product-search.feature",
        glue = {"victor.testing.spring.repo","cucumber.api.spring"})
public class ProductSearchFeature {

   @ClassRule
   public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
       .withDatabaseName("prop")
       .withUsername("postgres")
       .withPassword("password");
}

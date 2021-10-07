//package victor.testing.spring.repo;
//
//import io.cucumber.junit.CucumberOptions;
//import org.junit.ClassRule;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//// Why still on JUnit4: https://github.com/cucumber/cucumber-jvm/issues/1149#issuecomment-611716745
////@RunWith(Cucumber.class)
////@ExtendWith(CucuExte)
//@CucumberOptions(features = "classpath:product-search.feature",
//        glue = {"victor.testing.spring.repo","cucumber.api.spring"})
//public class ProductSearchFeature {
//
//   @ClassRule
//   public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
//       .withDatabaseName("prop")
//       .withUsername("postgres")
//       .withPassword("password");
//}

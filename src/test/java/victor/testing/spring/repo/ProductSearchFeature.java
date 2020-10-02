package victor.testing.spring.repo;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

// Why still on JUnit4: https://github.com/cucumber/cucumber-jvm/issues/1149#issuecomment-611716745
@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:product-search.feature",
        glue = {"victor.testing.spring.repo","cucumber.api.spring"})
public class ProductSearchFeature {
}

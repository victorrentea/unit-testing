package victor.testing.spring.repo;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:ro/victor/unittest/spring/repo/product-search.feature",
        glue = {"ro.victor.unittest.spring.repo","cucumber.api.spring"})
public class ProductSearchFeature {

}

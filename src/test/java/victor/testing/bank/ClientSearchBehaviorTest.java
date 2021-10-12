package victor.testing.bank;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:client-search.feature", glue={"ro.victor.unittest.bank","cucumber.api.spring"})
public class ClientSearchBehaviorTest {

}

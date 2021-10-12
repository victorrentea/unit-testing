package victor.testing.bdd.filtering;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:export-filters.feature"}, glue={"ro.victor.unittest.bdd.filtering"})
public class ExportFiltersBehaviorTest {

}

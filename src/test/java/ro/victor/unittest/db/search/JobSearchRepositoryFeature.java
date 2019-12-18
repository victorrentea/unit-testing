package ro.victor.unittest.db.search;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:job-search.feature", glue={"ro.victor.unittest.db.search","cucumber.api.spring"})
public class JobSearchRepositoryFeature {
}

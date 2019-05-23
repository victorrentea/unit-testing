package ro.victor.unittest.tdd;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.runtime.model.CucumberTagStatement;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:tennis-score.feature"}, glue={"ro.victor.unittest.tdd"})
public class TennisScoreFeature {

}

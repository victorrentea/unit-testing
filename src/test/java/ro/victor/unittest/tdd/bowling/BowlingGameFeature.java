package ro.victor.unittest.tdd.bowling;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:bowling.feature", glue = "ro.victor.unittest.tdd.bowling")
public class BowlingGameFeature {
}

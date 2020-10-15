package victor.testing.tdd;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:tennis-game.feature", glue = "victor.testing.tdd")
public class TennisGameFeature {

}

package victor.testing.tdd;

import org.assertj.core.api.Assertions;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:tennis-game.feature", glue = "victor.testing.tdd")
public class TennisGameFeature {
	
}

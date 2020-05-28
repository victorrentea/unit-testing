package ro.victor.unittest.tdd.tennis;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:tennis-game.feature",
    glue = "ro.victor.unittest.tdd")
public class TennisGameFeature {
}

package victor.testing.tdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:tennis-game.feature", glue = "victor.testing.tdd")
public class TennisGameFeature {
}

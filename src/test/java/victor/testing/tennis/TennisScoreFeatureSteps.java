package victor.testing.tennis;


import io.cucumber.java.en.Then;
import io.cucumber.junit.platform.engine.Cucumber;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Cucumber
public class TennisScoreFeatureSteps {
  @Autowired
  private CucumberTennisContext context;

  @Then("Score is {string}")
  public void the_score_is(String expectedScore) throws Throwable {
    assertThat(context.getTennisScore().score()).isEqualTo(expectedScore);
  }

}

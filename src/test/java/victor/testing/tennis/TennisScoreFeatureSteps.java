package victor.testing.tennis;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;

import static org.assertj.core.api.Assertions.assertThat;

@Cucumber
public class TennisScoreFeatureSteps {
  private TennisScore tennisScore;

  @Given("A new tennis game")
  public void a_new_tennis_game() throws Throwable {
    this.tennisScore = new TennisScore();
  }

}

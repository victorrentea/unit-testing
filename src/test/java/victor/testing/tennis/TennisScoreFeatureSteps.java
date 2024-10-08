package victor.testing.tennis;


import io.cucumber.java.en.And;
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

  // TODO delete & rewrite:
  @Then("Score is {string}")
  public void score_is(String expected) throws Throwable {
    assertThat(tennisScore.score()).isEqualTo(expected);
  }

  @When("Player{int} scores")
  public void playerScores(int playerNo) {
    tennisScore.addPoint(playerNo);
  }

  @And("Player{int} scores {int} points")
  public void playerScoresPoints(int playerNo, int points) {
    for (int i = 0; i < points; i++) {
      tennisScore.addPoint(playerNo);
    }
  }
}

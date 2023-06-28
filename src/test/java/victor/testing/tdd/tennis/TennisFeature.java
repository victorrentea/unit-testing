package victor.testing.tdd.tennis;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;

import static org.assertj.core.api.Assertions.assertThat;

@Cucumber
public class TennisFeature {
  private TennisScore tennisScore;

  @Given("A new tennis game")
  public void a_new_tennis_game() throws Throwable {
    tennisScore = new TennisScore();
  }
  // @Then("^Score is \"([^\"]*)\"$") // cucumber < 6
  @Then("Score is {string}")
  public void score_is(String expected) throws Throwable {
    assertThat(tennisScore.getScore()).isEqualTo(expected);
  }

  @When("Player{int} scores")
  public void playerScores(int playerNo) {
    addPointToPlayer(playerNo);
  }

  //    @And("^Player(\\d+) scores (\\d+) points$") // cucumber < 6
  @And("Player{int} scores {int} points") // cucumber < 6
  public void playerScoresPoints(int playerNo, int points) {
    for (int i = 0; i < points; i++) {
      addPointToPlayer(playerNo);
    }
  }

  private void addPointToPlayer(int playerNo) {
    if (playerNo == 1) {
      tennisScore.player1Scored();
    } else {
      tennisScore.player2Scored();
    }
  }
}


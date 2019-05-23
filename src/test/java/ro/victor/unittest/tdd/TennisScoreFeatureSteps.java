package ro.victor.unittest.tdd;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class TennisScoreFeatureSteps {

  TennisScore tennisScore;
  @Given("^An empty game$")
  public void an_empty_game() throws Throwable {
     tennisScore = new TennisScore();
  }

  @Then("^The score is \"([^\"]*)\"$")
  public void the_score_is(String expectedScoreString) throws Throwable {
    Assert.assertEquals(expectedScoreString, tennisScore.score());
  }

  @When("^Player(\\d+) scores a point$")
  public void player_scores_a_point(int playerNo) throws Throwable {
    if (playerNo == 1) {
      tennisScore.addPointPlayerOne();
    } else {
      tennisScore.addPointPlayerTwo();
    }
  }

  @When("^Player(\\d+) scores (\\d+) points$")
  public void player_scores_points(int playerNo, int poitnsNo) throws Throwable {
    for (int i = 0; i < poitnsNo; i++) {
      if (playerNo == 1) {
        tennisScore.addPointPlayerOne();
      } else {
        tennisScore.addPointPlayerTwo();
      }
    }
  }

}

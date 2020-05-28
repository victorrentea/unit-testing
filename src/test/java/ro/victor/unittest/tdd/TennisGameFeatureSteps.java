package ro.victor.unittest.tdd;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class TennisGameFeatureSteps {
  private TennisGame tennisGame;

  @Given("^an empty game$")
  public void anEmptyGame() {
    tennisGame = new TennisGame();
  }

  @Then("^the score is \"([^\"]*)\"$")
  public void theScoreIs(String expectedScore) throws Throwable {
    assertEquals(expectedScore, tennisGame.score());
  }

  @When("^Player1 scores$")
  public void playerScores() {
    tennisGame.addPoint(1);
  }

  @When("^both players scores$")
  public void bothPlayersScores() {
    tennisGame.addPoint(1);
    tennisGame.addPoint(2);
  }

  @When("^Player([1,2]) scores (\\d+) point[s]?$")
  public void playerScoresPoints(int playerNumber, int points) {
    for (int i = 0; i < points; i++) {
      tennisGame.addPoint(playerNumber);
    }
  }
}

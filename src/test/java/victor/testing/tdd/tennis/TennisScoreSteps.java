package victor.testing.tdd.tennis;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreSteps {

  private TennisScore tennisScore;

  @Given("A new tennis game")
  public void aNewTennisGame() {
    tennisScore = new TennisScore();
  }

  @Then("Score is {string}")
  public void scoreIs(String expectedScore) {
    assertThat(tennisScore.getScore()).isEqualTo(expectedScore);
  }

  @When("Player{int} scores")
  public void playerScores(int playerNumber) {
    Player player;
    if (playerNumber == 1) player = Player.ONE;
    else if (playerNumber == 2) player = Player.TWO;
    else throw new IllegalArgumentException("Bizule!! hai ma!!!");
    tennisScore.addPoint(player);
  }

  @When("Player{int} scores {int} points")
  public void playerScoresPoints(int playerNumber, int points) {
    for (int i = 0; i < points; i++) {
      playerScores(playerNumber);
    }
  }
}

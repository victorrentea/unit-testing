package victor.testing.tdd.tennis;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;

import static org.assertj.core.api.Assertions.assertThat;

@Cucumber
public class TennisSteps {

  private TennisGame tennisGame;

  @Given("A new tennis game")
  public void aNewTennisGame() {
    tennisGame = new TennisGame();
  }

  @When("Player{int} scores")
  public void playerScores(int playerNumber) {
    if (playerNumber == 1) {
      tennisGame.addPointToPlayer1();
    } else {
      tennisGame.addPointToPlayer2();
    }
  }

  @Then("Score is {string}")
  public void scoreIs(String expectedScore) {
    assertThat(tennisGame.getHumanReadableScore()).isEqualTo(expectedScore);
  }

  @When("Player{int} scores {int} points")
  public void playerScoresPoints(int playerNumber, int points) {
    for (int i = 0; i < points; i++) {
      if (playerNumber == 1) {
        tennisGame.addPointToPlayer1();
      } else {
        tennisGame.addPointToPlayer2();
      }
    }
  }
}

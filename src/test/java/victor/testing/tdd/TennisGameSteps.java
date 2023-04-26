package victor.testing.tdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import victor.testing.tdd.Player;
import victor.testing.tdd.TennisScore;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameSteps {

  private TennisScore tennisScore;

  @Given("A new tennis game")
  public void aNewTennisGame() {
    tennisScore = new TennisScore();
  }

  @Then("Score is {string}")
  public void scoreIs(String expectedScore) {
    assertThat(tennisScore.getScore())
            .isEqualTo(expectedScore);
  }

  @When("Player{int} scores")
  public void playerScores(int playerNumber) {
    Player player = playerNumber == 1 ? Player.ONE : Player.TWO;
    tennisScore.winsPoint(player);
  }
}

package victor.testing.tdd.tennis;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;

import static org.assertj.core.api.Assertions.assertThat;

@Cucumber
public class TennisGameFeatureSteps {

  private TennisGame tennisGame;

  @Given("A new tennis game")
  public void a_new_tennis_game() throws Throwable {
    System.out.println("HALO!");
    tennisGame = new TennisGame();
  }

  @Then("Score is {string}")
  public void scoreIs(String expectedScore) {
    assertThat(tennisGame.getScore()).isEqualTo(expectedScore);
  }

  @When("First Team scores")
  public void firstTeamScores() {
    tennisGame.addPointToFirstTeam();
  }

  @And("Second Team scores")
  public void secondTeamScores() {
    tennisGame.addPointToSecondTeam();
  }

  @And("Second Team scores {int} points")
  public void secondTeamScoresPoints(int points) {
    for (int i = 0; i < points; i++) {
      tennisGame.addPointToSecondTeam();
    }
  }

  @And("First Team scores {int} points")
  public void firstTeamScoresPoints(int points) {
    for (int i = 0; i < points; i++) {
      tennisGame.addPointToFirstTeam();
    }
  }
}

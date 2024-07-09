package victor.testing.tdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.*;

@Cucumber
public class TennisScoreGlueCode {

  public TennisScoreGlueCode() {
    System.out.println("New TennisScoreGlueCode instance created");
  }

  private TennisScore tennisScore;

  @Given("A new game")
  public void a_new_game() {
    // 20 lines of code to setup the game confguring it in unexpected weird ways
    tennisScore = new TennisScore(/*"John","Jane"*/);
  }

  @Then("The score is {string}")
  public void theScoreIs(String expectedScore) {
    // i can program mocks, insert into a db, configure a wiremock ,call my api, click a button with Selenium
    assertThat(tennisScore.getScore()).isEqualTo(expectedScore);
  }

  @When("Player {int} scores a point")
  public void playerScoresAPoint(int playerNumber) {
    if (playerNumber == 1) {
      tennisScore.addPointToPlayer1();
    } else {
      tennisScore.addPointToPlayer2();
    }
  }

  @When("Player {int} scores {int} points")
  public void playerScoresPoints(int playerNumber, int points) {
    for (int i = 0; i < points; i++) {
      if (playerNumber == 1) {
        tennisScore.addPointToPlayer1();
      } else {
        tennisScore.addPointToPlayer2();
      }
    }
  }
}

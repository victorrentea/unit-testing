package ro.victor.unittest.tdd;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameSteps {
   private TennisGame tennisGame;

   @Given("^An empty game$")
   public void anEmptyGame() {
      tennisGame = new TennisGame();
   }

   @Then("^The score is \"([^\"]*)\"$")
   public void theScoreIs(String expectedScore) throws Throwable {
      assertThat(tennisGame.score()).isEqualTo(expectedScore);
   }

   @When("^(\\w+) scores (\\d+) point$")
   public void playerScoresPoint(Players player, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.scoresPoint(player);
      }
   }
}

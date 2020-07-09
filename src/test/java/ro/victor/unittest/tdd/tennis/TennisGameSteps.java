package ro.victor.unittest.tdd.tennis;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class TennisGameSteps {

   private TennisGame tennisGame;

   @Given("^An empty game$")
   public void anEmptyGame() {
      tennisGame = new TennisGame();
   }

   @Then("^The score is \"([^\"]*)\"$")
   public void theScoreIs(String expectedScore) throws Throwable {
      Assert.assertEquals(expectedScore, tennisGame.score());
   }

   @When("^Player(\\d+) scores (\\d+) point$")
   public void playerScoresPoint(int playerNumber, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.addPoint(playerNumber);
      }
   }
}

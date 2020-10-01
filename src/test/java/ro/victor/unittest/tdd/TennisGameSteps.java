package ro.victor.unittest.tdd;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import ro.victor.unittest.tdd.TennisGame.Player;

public class TennisGameSteps {

   private TennisGame tennisGame;

   @Given("^a new tennis game$")
   public void aNewTennisGame() {
      tennisGame = new TennisGame();
   }

   @Then("^the score is \"([^\"]*)\"$")
   public void theScoreIs(String expectedScore) throws Throwable {
      String actualScore = tennisGame.score();
      Assert.assertEquals(expectedScore, actualScore);

   }

   @When("^player \"([^\"]*)\" scores (\\d+) points$")
   public void playerScoresPoints(Player player, int points) throws Throwable {
      for (int i = 0; i < points; i++) {
         tennisGame.markPointFor(player);
      }
   }
}

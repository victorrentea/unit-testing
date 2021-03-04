package victor.testing.tdd;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import victor.testing.tdd.TennisGame.Player;

public class TennisGameSteps {

   private TennisGame tennisGame;

   @Given("^A new game$")
   public void a_new_game() throws Throwable {
      tennisGame = new TennisGame();
   }

   @Then("^The score is \"([^\"]*)\"$")
   public void the_score_is(String expectedScore) throws Throwable {
      Assert.assertEquals(expectedScore, tennisGame.score());
   }

   @When("^Player(\\d+) scored a point$")
   public void playerScoredAPoint(int playerNumber) {
      Player player = Player.values()[playerNumber - 1];
      tennisGame.playerScores(player);
   }

   @When("^Player(\\d+) scored (\\d+) points$")
   public void playerScoredPoints(int playerNumber, int points) {
      Player player = Player.values()[playerNumber - 1];
      for (int i = 0; i < points; i++) {
         tennisGame.playerScores(player);
      }
   }
}

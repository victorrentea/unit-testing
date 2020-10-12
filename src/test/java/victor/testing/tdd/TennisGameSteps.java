package victor.testing.tdd;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;
import victor.testing.tdd.TennisGame.Player;

public class TennisGameSteps {
   private TennisGame tennisGame;

   @Given("^a new tennis game$")
   public void aNewTennisGame() {
      tennisGame = new TennisGame();
   }

   @Then("^the score is \"([^\"]*)\"$")
   public void theScoreIs(String expectedScore) throws Throwable {
      Assertions.assertThat(tennisGame.score()).isEqualTo(expectedScore);
   }

   @When("^Player \"([^\"]*)\" marks (\\d+) points$")
   public void playerMarksPoints(Player player, int points) throws Throwable {
      for (int i = 0; i < points; i++) {
         tennisGame.markPoint(player);
      }
   }
}
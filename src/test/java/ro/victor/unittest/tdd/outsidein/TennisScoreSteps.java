package ro.victor.unittest.tdd.outsidein;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;
import ro.victor.unittest.tdd.Player;
import ro.victor.unittest.tdd.TennisScore;

public class TennisScoreSteps {


   private TennisScore tennisScore;

   @Given("^An new game$")
   public void anNewGame() {
      tennisScore = new TennisScore();
   }

   @Then("^The score is \"([^\"]*)\"$")
   public void theScoreIs(String expectedScore) throws Throwable {
      Assertions.assertThat(tennisScore.score()).isEqualTo(expectedScore);
   }

   @When("^Player \"([^\"]*)\" scores$")
   public void playerScores(Player player) throws Throwable {
      tennisScore.pointWon(player);
   }

   @When("^Player \"([^\"]*)\" scores (\\d+)$")
   public void playerScoresPlayerPoints(Player player, int points) throws Throwable {
      for (int i =0;i<points;i++) {
         tennisScore.pointWon(player);
      }
   }
}

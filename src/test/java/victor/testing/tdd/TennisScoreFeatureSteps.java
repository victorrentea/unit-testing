package victor.testing.tdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

public class TennisScoreFeatureSteps {

   private TennisScore score;

   @Given("A new tennis game")
   public void aNewTennisGame() {
      score = new TennisScore();
   }

   @When("Player {string} scores {int} points")
   public void playerScoresPlayerPointsPoints(String playerStr, int points) {
      Player player = Player.valueOf(playerStr);
      for (int i = 0; i < points; i++) {
         score.winsPoint(player);
      }
   }

   @Then("Score is {string}")
   public void scoreIs(String expectedScore) {
      Assertions.assertThat(score.getScore()).isEqualTo(expectedScore);
   }
}

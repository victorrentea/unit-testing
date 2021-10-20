package victor.testing.tdd;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import victor.testing.tdd.TennisScore.Player;

public class TennisGameFeatureSteps {

   private TennisScore tennisScore;

   @Given("A new tennis game")
   public void aNewTennisGame() {
      tennisScore = new TennisScore();
   }

   @Then("Score is {string}")
   public void scoreIs(String expectedScore) {
      Assertions.assertThat(tennisScore.getScore()).isEqualTo(expectedScore);
   }

   @When("Player {string} scores {int} point")
   public void playerONEScoresPoint(String playerStr, int points) {
      Player player = Player.valueOf(playerStr);
      for (int i = 0; i < points; i++) {
         tennisScore.winsPoint(player);
      }
   }
}

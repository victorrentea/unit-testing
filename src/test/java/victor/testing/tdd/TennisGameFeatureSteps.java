//package victor.testing.tdd;
//
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.assertj.core.api.Assertions;
//import victor.testing.tdd.TennisScore.Player;
//
////@SpringBootTest
//public class TennisGameFeatureSteps {
//
//   private TennisScore tennisScore;
//
//   @Given("A new tennis game")
//   public void aNewTennisGame() {
//      tennisScore = new TennisScore();
//   }
////   @Given("User microservice returns user is active")
////   public void mock() {
////   wiremock.when(get()).thenreturn()
//////      when(userServiceMock.checkUserActive(any)).thenReturn(true)
////   }
////   @Given("Supplier id 3 is in database")
////   public void ock() {
////supplierRepo.save()
////   }
//
//   @Then("Score is {string}")
//   public void scoreIs(String expectedScore) {
//      Assertions.assertThat(tennisScore.getScore()).isEqualTo(expectedScore);
//   }
//
//   @When("Player {string} scores {int} point")
//   public void playerONEScoresPoint(String playerStr, int points) {
//      Player player = Player.valueOf(playerStr);
//      for (int i = 0; i < points; i++) {
//         tennisScore.winsPoint(player);
//      }
//   }
//}

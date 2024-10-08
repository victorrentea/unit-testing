package victor.testing.tennis;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;
import org.springframework.beans.factory.annotation.Autowired;

@Cucumber
public class TennisScoreFeatureSplitSteps {
  @Autowired
  private CucumberTennisContext context;

  @Given("A new tennis game")
  public void a_new_tennis_game() throws Throwable {
    context.setTennisScore(new TennisScore());
  }




  @When("Player{int} scores")
  public void player_scores(int player) throws Throwable {
    context.getTennisScore().addPoint(player);
  }

  @Given("Player{int} scores {int} points")
  public void player_scores_points(int playerNumber, int playerScore) {
    addPointsToPlayer(playerNumber, playerScore);
  }
  @Given("Player{int} scores points")
  public void player_scores_pointsBAD(int playerNumber) {
    // when(mock).thenReturn(...);
    addPointsToPlayer(playerNumber, 7);
  }


  private void addPointsToPlayer(int playerNumber, int playerScore) {
    for (int i = 0; i < playerScore; i++) {
      context.getTennisScore().addPoint(playerNumber);
    }
  }
}

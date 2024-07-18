package victor.testing.tdd;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.es.Cuando.Cuandos;
import io.cucumber.junit.platform.engine.Cucumber;
import org.assertj.core.api.Assertions;

@Cucumber

public class TennisScoreSteps {

   private TennisScore tennisGame;

    @Given("A new tennis game")
    public void a_new_tennis_game() throws Throwable {
        tennisGame = new TennisScore();
    }
    // @Then("^Score is \"([^\"]*)\"$") // cucumber < 6
    @Then("Score is {string}")
    public void score_is(String expected) throws Throwable {
        Assertions.assertThat(tennisGame.getScore()).isEqualTo(expected);
    }

    @When("Player{int} scores")
    public void playerScores(int playerNo) {
        tennisGame.addPoint(playerNo);
    }

//    @And("^Player(\\d+) scores (\\d+) points$") // cucumber < 6
    @And("Player{int} scores {int} points") // cucumber < 6
    public void playerScoresPoints(int playerNo, int points) {
        for (int i = 0; i < points; i++) {
            tennisGame.addPoint(playerNo);
        }
    }
}

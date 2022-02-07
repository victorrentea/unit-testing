package victor.testing.tdd.classic;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;

import static org.junit.Assert.assertEquals;

@Cucumber
public class TennisGameFeatureSteps {
    private TennisGame tennisGame;

    @Given("A new tennis game")
    public void a_new_tennis_game() throws Throwable {
        tennisGame = new TennisGame();
    }
    // @Then("^Score is \"([^\"]*)\"$") // cucumber < 6
    @Then("Score is {string}")
    public void score_is(String expected) throws Throwable {
        assertEquals(expected, tennisGame.getScore());
    }

    @When("Player {string} scores")
    public void playerScores(String playerStr) {
        Player player = Player.valueOf(playerStr);
        tennisGame.addPoint(player);
    }

//    @And("^Player(\\d+) scores (\\d+) points$") // cucumber < 6
    @And("Player {string} scores {int} points") // cucumber < 6
    public void playerScoresPoints(String playerStr, int points) {
        Player player = Player.valueOf(playerStr);

        for (int i = 0; i < points; i++) {
            tennisGame.addPoint(player);
        }
    }
}

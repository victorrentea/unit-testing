package victor.testing.tdd;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import victor.testing.tdd.TennisGame.Player;

import static org.junit.Assert.assertEquals;

public class TennisGameFeatureSteps {
    private TennisGame tennisGame;

    @Given("A new tennis game")
    public void a_new_tennis_game() throws Throwable {
        tennisGame = new TennisGame();
    }
    // @Then("^Score is \"([^\"]*)\"$") // cucumber < ver 6
    @Then("Score is {string}")
    public void score_is(String expected) throws Throwable {
        assertEquals(expected, tennisGame.getScore());
    }

    @When("Player {string} scores")
    public void playerScores(String playerName) {
        tennisGame.playerWonPoint(Player.valueOf(playerName));
    }

//    @And("^Player(\\d+) scores (\\d+) points$") // cucumber < 6
    @And("Player {string} scores {int} points") // cucumber < 6
    public void playerScoresPoints(String playerName, int points) {
        for (int i = 0; i < points; i++) {
            tennisGame.playerWonPoint(Player.valueOf(playerName));

        }
    }
}

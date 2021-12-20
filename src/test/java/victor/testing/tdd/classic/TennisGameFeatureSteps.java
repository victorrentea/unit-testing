package victor.testing.tdd.classic;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import victor.testing.tdd.classic.TennisScore.Player;

import static org.junit.Assert.assertEquals;

public class TennisGameFeatureSteps {
    private TennisScore tennisGame;

    @Given("A new tennis game")
    public void a_new_tennis_game() throws Throwable {
        tennisGame = new TennisScore();
    }
    // @Then("^Score is \"([^\"]*)\"$") // cucumber < 6
    @Then("Score is {string}")
    public void score_is(String expected) throws Throwable {
        assertEquals(expected, tennisGame.currentScore());
    }

    @When("Player{int} scores")
    public void playerScores(int playerNo) {
        tennisGame.winsPointByPlayer(Player.values()[playerNo]);
    }

//    @And("^Player(\\d+) scores (\\d+) points$") // cucumber < 6
    @And("Player{int} scores {int} points") // cucumber < 6
    public void playerScoresPoints(int playerNo, int points) {
        for (int i = 0; i < points; i++) {
            tennisGame.winsPointByPlayer(Player.values()[i]);
        }
    }

}

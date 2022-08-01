package victor.testing.tdd.classic;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertEquals;

@Cucumber
public class TennisGameFeatureSteps {
    private TennisScore tennisGame;

    @Given("A new tennis game")
    public void a_new_tennis_game() throws Throwable {
        tennisGame = new TennisScore();
    }
    // @Then("^Score is \"([^\"]*)\"$") // cucumber < 6
    @Then("Score is {string}")
    public void score_is(String expected) throws Throwable {
        Assertions.assertEquals(expected, tennisGame.getScore());
    }

    @When("Player{int} scores")
    public void playerScores(int playerNo) {
        if (playerNo == 1) {
            tennisGame.wonPoint(Player.ONE);
        } else {
            tennisGame.wonPoint(Player.TWO);
        }
    }

//    @And("^Player(\\d+) scores (\\d+) points$") // cucumber < 6
    @And("Player{int} scores {int} points") // cucumber < 6
    public void playerScoresPoints(int playerNo, int points) {
        for (int i = 0; i < points; i++) {
            playerScores(playerNo);
        }
    }
}

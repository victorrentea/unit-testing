package ro.victor.unittest.tdd.tennis;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

public class TennisGameSteps {
    private TennisGame tennisGame;
    @Given("^A new game$")
    public void a_new_game() throws Throwable {
        tennisGame = new TennisGame();
    }

    @Then("^Score is \"([^\"]*)\"$")
    public void score_is(String expectedScoreString) throws Throwable {
        String actualScore = tennisGame.score();
        assertEquals(expectedScoreString, actualScore);
    }

    @When("^Player(\\d+) scores one point$")
    public void playerScoresOnePoint(int playerNumber) {
        tennisGame.scorePoint(playerNumber);
    }

    @When("^Player(\\d+) scores (\\d+) points$")
    public void playerScoresPoints(int playerNumber, int points) {
        for (int i = 0; i< points;i++) {
            tennisGame.scorePoint(playerNumber);
        }
    }
}

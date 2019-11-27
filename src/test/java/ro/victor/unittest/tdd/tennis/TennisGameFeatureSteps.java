package ro.victor.unittest.tdd.tennis;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class TennisGameFeatureSteps {

    private TennisGame tennisGame;

    @Given("^A new Game$")
    public void a_new_Game() throws Throwable {
        tennisGame = new TennisGame();
    }

    @Then("^Score is \"([^\"]*)\"$")
    public void score_is(String expectedScore) throws Throwable {
        Assert.assertEquals(expectedScore,tennisGame.score());
    }

    @When("^Player(\\d+) scores$")
    public void playerScores(int playerNumber) {
        tennisGame.playerScores(playerNumber);
    }

    @When("^Player(\\d+) scores \"([^\"]*)\" points$")
    public void playerScoresPoints(int playerNumber, int points) {
        for (int i = 0; i < points; i++) {
            tennisGame.playerScores(playerNumber);
        }
    }

}

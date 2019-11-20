package ro.victor.unittest.tdd;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameSteps {

    private TennisGame tennisGame;

    @Given("^An empty game$")
    public void an_empty_game() {
        tennisGame = new TennisGame();
    }

    @Then("^Score is \"([^\"]*)\"$")
    public void score_is(String expectedScore) {
        assertThat(tennisGame.score()).isEqualTo(expectedScore);
    }

    @When("^Player(\\d+) scores (\\d+) point$")
    public void playerScoresPoint(int playerNo, int points) {
        for (int i = 0; i < points; i++) {
            tennisGame.point(playerNo);
        }
    }
}

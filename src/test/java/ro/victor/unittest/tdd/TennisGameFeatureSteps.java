package ro.victor.unittest.tdd;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameFeatureSteps {
    private TennisGame tennisGame;

    @Given("^An empty game$")
    public void an_empty_game() throws Throwable {
        tennisGame = new TennisGame();
    }

    @Then("^The score is \"([^\"]*)\"$")
    public void the_score_is(String arg1) throws Throwable {
        assertThat(tennisGame.score()).isEqualTo(arg1);
    }

    @When("^Player(\\d+) scores (\\d+) point$")
    public void playerScoresPoint(int playerNo, int points) {
        for (int i = 0; i < points; i++) {
            tennisGame.addPoint(playerNo);
        }
    }
}

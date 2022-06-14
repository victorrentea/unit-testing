package victor.testing.tdd.classic;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import victor.testing.bank.entity.Client;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisTestSteps {

    private TennisScore tennisScore;

    @Given("A new tennis game")
    public void aNewTennisGame() {
        tennisScore = new TennisScore();
    }

    @Then("Score is {string}")
    public void scoreIs(String expectedScore) {
        assertThat(tennisScore.getScore()).isEqualTo(expectedScore);
    }

    @When("Player{int} scores")
    public void playerScores(int playerNumber) {
        tennisScore.winPoint(playerNumber - 1);
    }

    @When("Player{int} scores {int} points")
    public void playerScoresPoints(int playerNumber, int points) {
        for (int i = 0; i < points; i++) {
            tennisScore.winPoint(playerNumber - 1);
        }
    }
}

package victor.testing.tdd.tennis;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Cucumber
public class TennisGameFeatureSteps {
    private TennisGame tennisGame;
//@Test
//void explore() {
//    // mini-testing framework, code not string.
//    a_new_tennis_game();
//    score_is("Love-Love");
//}
    @Given("A new tennis game")
    public void a_new_tennis_game() throws Throwable {
        tennisGame = new TennisGame();
    }
    // @Then("^Score is \"([^\"]*)\"$") // cucumber < 6
    @Then("Score is {string}")
    public void score_is(String expected) throws Throwable {
        assertThat(tennisGame.getScore()).isEqualTo(expected);
    }

    @When("Player{int} scores")
    public void playerScores(int playerNo) {
        tennisGame.addPoint(playerNo==1 ? Player.ONE : Player.TWO);
    }

//    @And("^Player(\\d+) scores (\\d+) points$") // cucumber < 6
    @And("Player{int} scores {int} points") // cucumber < 6
    public void playerScoresPoints(int playerNo, int points) {
        for (int i = 0; i < points; i++) {
            tennisGame.addPoint(playerNo==1 ? Player.ONE : Player.TWO);
        }
    }
}

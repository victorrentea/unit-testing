package victor.testing.tdd.classic;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

//@Cucumber
public class TennisScoreFeatureSteps {
    private TennisScore tennisScore;

//    @Test
//    void explore() {
//        givenANew
//    }
    @Given("A new tennis game")
    public void a_new_tennis_game() throws Throwable {
        tennisScore = new TennisScore();
    }
    // @Then("^Score is \"([^\"]*)\"$") // cucumber < 6
    @Then("Score is {string}")
    public void score_is(String expected) throws Throwable {
        assertEquals(expected, tennisScore.getScore());
    }

//    @When("Player{int} scores")
//    public void playerScores(int playerNo) {
//        tennisScore.addPoint(playerNo);
//    }
//
////    @And("^Player(\\d+) scores (\\d+) points$") // cucumber < 6
//    @And("Player{int} scores {int} points") // cucumber < 6
//    public void playerScoresPoints(int playerNo, int points) {
//        for (int i = 0; i < points; i++) {
//            tennisScore.addPoint(playerNo);
//        }
//    }

    @When("Player {string} scores {int} points")
    public void playerScoresPoints(String playerStr, int points) {
        Player player = Player.valueOf(playerStr);
        for (int i = 0; i < points; i++) {
            tennisScore.addPoint(player);
        }
    }

    @When("Player {string} scores")
    public void playerScores(String playerStr) {
        Player player = Player.valueOf(playerStr);
        tennisScore.addPoint(player);
    }
}

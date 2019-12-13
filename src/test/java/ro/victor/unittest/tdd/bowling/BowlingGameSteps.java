package ro.victor.unittest.tdd.bowling;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BowlingGameSteps {

    private int actualScore;

    @When("^The sequence of rolls is \"([^\"]*)\"$")
    public void the_row_is(String rowStr) throws Throwable {
        String[] numberStrings = rowStr.split(" ");

        int[] ints = new int[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            ints[i] = Integer.parseInt(numberStrings[i]);
        }
        actualScore = BowlingGame.calculateScore(ints);
    }

    @Then("^The score is (\\d+)$")
    public void the_score_is(int expectedScore) throws Throwable {
        Assertions.assertThat(actualScore).isEqualTo(expectedScore);
    }

    @And("^Means a \"([^\"]*)\"$")
    public void meansA(String arg0) throws Throwable {
    }
}

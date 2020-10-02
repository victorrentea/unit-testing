package victor.testing.tdd.bowling;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BowlingGameSteps {

    private int actualScore;

    @When("^The sequence of rolls is \"([^\"]*)\"$")
    public void the_row_is(String rowStr) throws Throwable {

        List<Integer> ints = Stream.of(rowStr.split(" "))
                .map(Integer::parseInt)
                .collect(toList());

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

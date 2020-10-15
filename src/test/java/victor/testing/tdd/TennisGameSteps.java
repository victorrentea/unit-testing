package victor.testing.tdd;

import org.assertj.core.api.Assertions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import victor.testing.tdd.TennisGame.Player;

public class TennisGameSteps {
	private TennisGame tennisGame;

	@Given("^A new tennis game$")
	public void a_new_tennis_game() throws Throwable {
		tennisGame = new TennisGame();
	}

	@Then("^Score is \"([^\"]*)\"$")
	public void score_is(String expectedScore) throws Throwable {
		Assertions.assertThat(tennisGame.score()).isEqualTo(expectedScore);
	}

	@When("^Player(\\d+) scores$")
	public void player_scores(int playerIndex) throws Throwable {
		Player player = playerIndex == 1?Player.ONE:Player.TWO;
		tennisGame.playerScoresPoint(player);
	}

	@When("^Player(\\d+) scores (\\d+) points$")
	public void player_scores_points(int playerIndex, int pointsNumber) throws Throwable {
		Player player = playerIndex == 1?Player.ONE:Player.TWO;
		for (int i=0;i<pointsNumber; i++) {
			tennisGame.playerScoresPoint(player);
		}
	}
}

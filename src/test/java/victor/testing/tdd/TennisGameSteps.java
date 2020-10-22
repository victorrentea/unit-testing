package victor.testing.tdd;

import static org.junit.Assert.assertEquals;

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
		assertEquals(expectedScore, tennisGame.score());
	}

	@When("^Player(\\d+) scores$")
	public void player_scores(int playerIndex) throws Throwable 
	{
		Player player = playerIndex == 1 ? Player.ONE :Player.TWO;
		tennisGame.scorePoint(player);
	}

	@When("^Player(\\d+) scores (\\d+) points$")
	public void player_scores_points(int playerIndex, int points) throws Throwable {
		Player player = playerIndex == 1 ? Player.ONE :Player.TWO;
		for (int i=0;i<points;i++) {
			tennisGame.scorePoint(player);
		}
	}

}

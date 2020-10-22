package victor.testing.tdd;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import victor.testing.tdd.TennisGame.Player;

public class TennisGameTest {
	// The running score of each game is described in a manner peculiar to tennis:
	// scores from zero to three points are described as "Love", "Fifteen",
	// "Thirty", and "Forty" respectively.
	
	private TennisGame tennisGame = new TennisGame();

	// TODO: tennisGame pe @Before
	// TODO metoda care joaca X pct pe un player. ca sa fie mai reada : 
	private void addPoints(Player player, int points) {
		for(int i =0;i<points; i++) {
			tennisGame.scorePoint(player);
		}
	}
	
	@Test
	public void newGame() throws Exception {
		String score = tennisGame.score();
		assertEquals("Love-Love", score);
	}
	@Test
	public void fifteenLove() throws Exception {
		addPoints(Player.ONE, 1);
		String score = tennisGame.score();
		assertEquals("Fifteen-Love", score);
	}
	@Test
	public void thirtyLove() throws Exception {
		addPoints(Player.ONE, 2);
		String score = tennisGame.score();
		assertEquals("Thirty-Love", score);
	}

}

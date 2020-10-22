package victor.testing.tdd;

import static org.junit.Assert.*;

import org.junit.Test;

import victor.testing.tdd.TennisGame.Player;

public class TennisGameTest {
	// The running score of each game is described in a manner peculiar to tennis:
	// scores from zero to three points are described as "Love", "Fifteen",
	// "Thirty", and "Forty" respectively.
	
	
	// TODO: tennisGame pe @Before
	// 
	
	@Test
	public void newGame() throws Exception {
		String score = new TennisGame().score();
		assertEquals("Love-Love", score);
	}
	@Test
	public void fifteenLove() throws Exception {
		TennisGame tennisGame = new TennisGame();
		tennisGame.scorePoint(Player.ONE);
		String score = tennisGame.score();
		assertEquals("Fifteen-Love", score);
	}
	@Test
	public void thirtyLove() throws Exception {
		TennisGame tennisGame = new TennisGame();
		tennisGame.scorePoint(Player.ONE);
		tennisGame.scorePoint(Player.ONE);
		String score = tennisGame.score();
		assertEquals("Thirty-Love", score);
	}

}

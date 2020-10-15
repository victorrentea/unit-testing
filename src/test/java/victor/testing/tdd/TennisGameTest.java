package victor.testing.tdd;

import static org.junit.Assert.*;

import org.junit.Test;

import victor.testing.tdd.TennisGame.Player;

public class TennisGameTest {

	// The running score of each game is described in a manner peculiar to tennis:
	// scores from zero to three points are described as "Love", "Fifteen",
	// "Thirty", and "Forty" respectively.
	
	private TennisGame tennisGame = new TennisGame();

	@Test
	public void loveLove() throws Exception {
		String score = tennisGame.score();
		assertEquals("Love:Love", score); 
	}
	@Test
	public void loveFifteen() throws Exception {
		tennisGame.playerScoresPoint(Player.TWO);
		String score = tennisGame.score();
		assertEquals("Love:Fifteen", score); 
	}
	@Test
	public void fifteenLove() throws Exception {
		tennisGame.playerScoresPoint(Player.ONE);
		String score = tennisGame.score();
		assertEquals("Fifteen:Love", score); 
	}
	
	@Test
	public void loveThirty() throws Exception {
		tennisGame.playerScoresPoint(Player.TWO);
		tennisGame.playerScoresPoint(Player.TWO);
		String score = tennisGame.score();
		assertEquals("Love:Thirty", score); 
	}
	
	@Test // Although overlapping, I leave this is my tests
	// because it's interesting. remakable. for specification purposes.
	public void fifteenFifteen() throws Exception {
		tennisGame.playerScoresPoint(Player.ONE);
		tennisGame.playerScoresPoint(Player.TWO);
		String score = tennisGame.score();
		assertEquals("Fifteen:Fifteen", score); 
	}
	
//	@Test // duplicated overlapping test. DELETE THIS
//	public void fifteenThirty() throws Exception {
//		tennisGame.playerScoresPoint(Player.ONE);
//		tennisGame.playerScoresPoint(Player.TWO);
//		tennisGame.playerScoresPoint(Player.TWO);
//		String score = tennisGame.score();
//		assertEquals("Fifteen:Thirty", score); 
//	}
	@Test
	public void loveForty() throws Exception {
		tennisGame.playerScoresPoint(Player.TWO);
		tennisGame.playerScoresPoint(Player.TWO);
		tennisGame.playerScoresPoint(Player.TWO);
		String score = tennisGame.score();
		assertEquals("Love:Forty", score); 
	}
	
	@Test
	public void player1Wins_4_0() throws Exception {
	}
	@Test
	public void player1Wins_5_3() throws Exception {
	}
	@Test
	public void player2Wins_0_4() throws Exception {
	}	
	@Test
	public void player2Wins_13_15() throws Exception {
	}
	@Test
	public void deuce_3_3() throws Exception {
	}
	@Test
	public void deuce_4_4() throws Exception {
	}
	@Test
	public void advantage1_4_3() throws Exception {
	}
	@Test
	public void advantage2_3_4() throws Exception {
	}
	@Test
	public void advantage1_15_14() throws Exception {
	}
	
	
	
	
	
}

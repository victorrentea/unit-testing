package victor.testing.tdd;

import static org.junit.Assert.*;

import org.junit.Test;

import victor.testing.tdd.TennisGame.Player;

public class TennisGameTest {

	// The running score of each game is described in a manner peculiar to tennis:
	// scores from zero to three points are described as "Love", "Fifteen",
	// "Thirty", and "Forty" respectively.
	

	// mini-test frameworks come at the beginning
	private String getScore(int player1Points, int player2Points) {
		TennisGame tennisGame = new TennisGame();
		setPointsForPlayer(player1Points, Player.ONE, tennisGame);
		setPointsForPlayer(player2Points, Player.TWO, tennisGame);
		return tennisGame.score();
	}
	private void setPointsForPlayer(int points, Player player, TennisGame tennisGame) {
		for (int i = 0; i < points; i ++) {
			tennisGame.playerScoresPoint(player);
		}
	}
	
	
	@Test
	public void loveLove() throws Exception {
		assertEquals("Love:Love", getScore(0, 0)); 
	}
	@Test
	public void loveFifteen() throws Exception {
		assertEquals("Love:Fifteen", getScore(0, 1)); 
	}
	@Test
	public void fifteenLove() throws Exception {
		assertEquals("Fifteen:Love", getScore(1, 0)); 
	}
	
	@Test
	public void loveThirty() throws Exception {
		assertEquals("Love:Thirty", getScore(0, 2)); 
	}
	
	@Test // Although overlapping, I leave this is my tests
	// because it's interesting. remakable. for specification purposes.
	public void fifteenFifteen() throws Exception {
		assertEquals("Fifteen:Fifteen", getScore(1, 1)); 
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
		assertEquals("Love:Forty", getScore(0, 3)); 
	}
	
	@Test
	public void player1Wins_4_0() throws Exception {
		assertEquals("Game Won by Player 1", getScore(4,0)); 
	}
	@Test
	public void player1Wins_5_3() throws Exception {
		assertEquals("Game Won by Player 1", getScore(5, 3)); 
	}
	
//	@Test
//	public void player2Wins_0_4() throws Exception {
//	}	
//	@Test
//	public void player2Wins_13_15() throws Exception {
//	}
//	@Test
//	public void deuce_3_3() throws Exception {
//	}
//	@Test
//	public void deuce_4_4() throws Exception {
//	}
//	@Test
//	public void advantage1_4_3() throws Exception {
//	}
//	@Test
//	public void advantage2_3_4() throws Exception {
//	}
//	@Test
//	public void advantage1_15_14() throws Exception {
//	}
	
	
	
	
	
}

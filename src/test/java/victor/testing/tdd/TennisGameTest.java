package victor.testing.tdd;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import victor.testing.tdd.TennisGame.Player;

public class TennisGameTest {
	// The running score of each game is described in a manner peculiar to tennis:
	// scores from zero to three points are described as "Love", "Fifteen",
	// "Thirty", and "Forty" respectively.
	

	// TODO: tennisGame pe @Before
	// TODO metoda care joaca X pct pe un player. ca sa fie mai reada : 
	private void addPoints(Player player, int points, TennisGame tennisGame) {
		for(int i =0;i<points; i++) {
			tennisGame.scorePoint(player);
		}
	}
	
	// pure function pt ca 1) intoarce acelasi rez pt aceleasi inputuri. 2) nu modifica nimic in exteriorul ei.
	private String getScore(int player1Points, int player2Points ) {
		TennisGame tennisGame = new TennisGame();
		addPoints(Player.ONE, player1Points, tennisGame);
		addPoints(Player.TWO, player2Points, tennisGame);
		return tennisGame.score();
	}
	
	@Test
	public void thirtyLove() throws Exception {
		assertEquals("Thirty-Love", getScore(2,0));
		assertEquals("Thirty-Love", getScore(2,0));
		assertEquals("Thirty-Love", getScore(2,0));
	}
	
	
	
	
	///////
	
	
	
	
	
	@Test
	public void newGame() throws Exception {
		assertEquals("Love-Love", getScore(0, 0));
	}
	@Test
	public void fifteenLove() throws Exception {
		assertEquals("Fifteen-Love", getScore(1, 0));
	}
	
	@Test
	public void loveFifteen() throws Exception {
		assertEquals("Love-Fifteen", getScore(0, 1));
	}
	
	//test overlapping (gata verde cand l-am scris cu TDD) pe care il las cu rol de documentare. Caz remarcabil de biz
	@Test
	public void fifteenFifteen() throws Exception {
		assertEquals("Fifteen-Fifteen", getScore(1,1));
	}
	
//	c) love-fifteen
	
	@Test
	public void fortyLove() throws Exception {
//		addPoints(Player.ONE, 3);
//		String score = tennisGame.score();
		assertEquals("Forty-Love", getScore(3,1));
	}
	
	@Test
	public void deuce() throws Exception {
		assertEquals("Deuce", getScore(3, 3));
	}
	@Test
	public void deuce4() throws Exception {
		assertEquals("Deuce", getScore(4, 4));
	}
	
	@Test
	public void advantagePlayer1() throws Exception {
		assertEquals("Advantage Player1", getScore(5, 4));
		
	}
	@Test
	public void advantagePlayer2() throws Exception {
		assertEquals("Advantage Player2", getScore(3, 4));
	}
	
	@Test
	public void laRupt() throws Exception {
		assertEquals("Game Won Player1", getScore(4, 0));
	}
	@Test
	public void laRupt2() throws Exception {
		assertEquals("Game Won Player2", getScore(0, 4));
	}
	@Test
	public void gameWonPlayer1_lamultepuncte() throws Exception {
		assertEquals("Game Won Player1", getScore(10, 8));
	}
	

}

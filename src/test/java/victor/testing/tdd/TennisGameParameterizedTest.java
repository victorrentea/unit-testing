package victor.testing.tdd;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import victor.testing.tdd.TennisGame.Player;

@RunWith(Parameterized.class)
public class TennisGameParameterizedTest {
	
	private final int player1Points;
	private final int player2Points;
	private final String expectedScore;

	public TennisGameParameterizedTest(int player1Points, int player2Points, String expectedScore) {
		this.player1Points = player1Points;
		this.player2Points = player2Points;
		this.expectedScore = expectedScore;
	}

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
	public void testulUnic() throws Exception {
		assertEquals(expectedScore, getScore(player1Points,player2Points));
	}
	
	// <xmlIntra> --XSLT--> <xmlCuOStructIdenticaCuEntityModel> --JAXB--> unmarshal in instante
	
	// sample1.in.xml / sample1.out.xml
	
	@Parameters(name = "When Player1 scores {0} points and Player2 scores {1} then score is {2}")
	public static java.util.List<Object[]> m() {
		
//		File folder = new File();
//	for (iFile : folder.list()) { gasesti out file dupa regula; rulezi transformata si verifici XML de out.
		return asList(
				new Object[] {1,1,"Fifteen-Fifteen"},
				new Object[] {0,1,"Love-Fifteen"},
				new Object[] {0,0,"Love-Love"});
		
	}
}

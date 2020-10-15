package victor.testing.tdd;

public class TennisGame {
	private int player1Points;
	private int player2Points;

	enum Player {
		ONE, TWO 
	}

	public String score() {
		return map(player1Points) + ":" + map(player2Points);
	}
	private final static String[] SCORE_LABELS = {"Love","Fifteen", "Thirty","Forty"};
	
	private String map(int points) {
		// Other implem Ideas: switch; map.get(points);   ARRAY[points];  enum
		return SCORE_LABELS[points];
	}

	public void playerScoresPoint(Player player) {
		
		if (player == Player.TWO) {
			player2Points ++;
		} else {
			player1Points ++;
		}
		
	}

}

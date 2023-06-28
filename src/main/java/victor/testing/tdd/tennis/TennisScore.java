package victor.testing.tdd.tennis;

import java.util.Map;

public class TennisScore {

  private int player1Points = 0;
  private int player2Points = 0;
  private static final Map<Integer, String> pointsToScore = Map.of(
      0, "Love",
      1, "Fifteen",
      2, "Thirty",
      3, "Forty"
      );

  public String getScore() {
    return pointsToScore.get(player1Points) + "-" +
           pointsToScore.get(player2Points);
  }

  public void player1Scored() {
    player1Points++;
  }

  public void player2Scored() {
    player2Points++;
  }
}

package victor.testing.tdd.tennis;

import java.util.Map;

public class TennisScore {

  private int player1Points = 0;
  private static final Map<Integer, String> pointsToScore = Map.of(
      0, "Love",
      1, "Fifteen",
      2, "Thirty"
//      3, "Forty" // UNTESTED BEHAVIOR!!!!!!!!
      );

  public String getScore() {
    return pointsToScore.get(player1Points) + "-Love";
  }

  public void player1Scored() {
    player1Points++;
  }
}

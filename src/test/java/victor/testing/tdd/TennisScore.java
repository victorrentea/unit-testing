package victor.testing.tdd;

import java.util.Map;

public class TennisScore {
  private static final String[] scoreLabels = {"Love", "Fifteen", "Thirty"};
  private int player1Points;
  private int player2Points;

  public String getScore() {
    return scoreLabels[player1Points] + "-Love";
  }

  public void winsPoint(Player player) {
    player1Points++;
  }
}

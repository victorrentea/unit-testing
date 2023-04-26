package victor.testing.tdd;

import java.util.Map;

public class TennisScore {
  private static final String[] scoreLabels = {"Love", "Fifteen", "Thirty", "Forty"};
  private int player1Points;
  private int player2Points;

  public String getScore() {
    if (Math.abs(player1Points - player2Points) == 1 &&
        player1Points >= 3 &&
        player2Points >= 3) {
      return "Advantage";
    }
    if (player1Points == player2Points &&
        player1Points >= 3) {
      return "Deuce";
    }
    return scoreLabels[player1Points] + "-" + scoreLabels[player2Points];
  }

  public void winsPoint(Player player) {
    if (player == Player.ONE) {
      player1Points++;
    } else {
      player2Points++;
    }
  }
}

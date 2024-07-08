package victor.testing.tdd;

import java.util.Map;

public class TennisScore {
  private int player1Points = 0;
  private int player2Points = 0;

  private static final String[] LABELS =
      {"Love", "Fifteen", "Thirty", "Forty"};

  public String getScore() {
    if (player1Points >= 4 && player1Points - player2Points >= 2) {
      return "Game won Player1";
    }
    if (player1Points == player2Points) {
      if (player1Points >= 3) {
        return "Deuce";
      } else {
        return LABELS[player1Points] + " - All";
      }
    }
    return LABELS[player1Points] + " - " + LABELS[player2Points];
  }

  public void addPointToPlayer1() {
    player1Points++;
  }

  public void addPointToPlayer2() {
    player2Points++;
  }
}

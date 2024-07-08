package victor.testing.tdd;

import java.util.Map;

public class TennisScore {
  private int player1Points = 0;

  private static final String[] LABELS = {"Love", "Fifteen", "Thirty"};

  public String getScore() {
    String player1Score = LABELS[player1Points];
    return player1Score+" - Love";
  }

  public void addPointToPlayer1() {
    player1Points++;
  }
}

package victor.testing.tdd;

import java.util.Map;

public class TennisScore {

  private String string = "Love - Love";
  private int player1Points = 0;

  private static final String[] LABELS = {"Love", "Fifteen", "Thirty"};

  public String getScore() {
    String player1Score = LABELS[player1Points];
    return player1Score+"-Love";
  }

  public void addPointToPlayer1() {
    player1Points++;
    if (string.equals("Love - Love")) {
      string = "Fifteen - Love";
    } else if (string.equals("Fifteen - Love")) {
      string = "Thirty - Love";
    }
  }
}

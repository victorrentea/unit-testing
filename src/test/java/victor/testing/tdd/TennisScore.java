package victor.testing.tdd;

import java.util.Map;

public class TennisScore {
//Map<player, String >
//  LinkedList
  private static final String[] scoreLabels = {"Love", "Fifteen", "Thirty"};
  private int player1Points;
  private int player2Points;
  private String score = "Love-Love";

  public String getScore() {
    return scoreLabels[player1Points] + "-Love";
//    return score;
  }

  public void winsPoint(Player player) {
    if (score.equals("Fifteen-Love")) {
      score = "Thirty-Love";
    } else {
      score = "Fifteen-Love";
    }
    // if todo
    player1Points++;
  }
}

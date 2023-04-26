package victor.testing.tdd;

import java.util.Map;

public class TennisScore {

  private String score = "Love-Love";

  public String getScore() {
    return score;
  }

  public void winsPoint(Player player) {
    if (score.equals("Fifteen-Love")) {
      score = "Thirty-Love";
    } else {
      score = "Fifteen-Love";
    }
  }
}

package victor.testing.tdd;

public class TennisScore {

  private String score = "Love-Love";

  public String getScore() {
    return score;
  }

  public void winsPoint(Player player) {
    score = "Fifteen-Love";
  }
}

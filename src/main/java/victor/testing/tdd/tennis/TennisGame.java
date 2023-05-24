package victor.testing.tdd.tennis;

public class TennisGame {

  private String score = "Love-Love";

  public String getScore() {
    return score;
  }

  public void addPoint(Player player) {
    score = "Fifteen-Love";
  }
}

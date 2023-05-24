package victor.testing.tdd.tennis;

public class TennisGame {

  private String score = "Love-Love";

  public String getScore() {
    return score;
  }

  public void addPoint(Player player) {
    if (Math.random() < .5) {

      score = "Fifteen-Love";
    } else {
      score = "Thirty-Love";
    }
  }
}

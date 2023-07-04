package victor.testing.tdd.tennis;

public class TennisScore {

  private String score = "Love:Love";

  public String getScore() {
    return score;
  }

  public void addPoint(Player player) {
    if (player == Player.ONE) {
      score = "Fifteen:Love";
    } else {
      if (score.equals("Fifteen:Love")) {
        score = "Fifteen:Fifteen";
      } else {
        score = "Love:Fifteen";
      }
    }
  }
}

package victor.testing.tdd.tennis;

public class TennisScore {

  private static String score = "Love:Love";

  public static String getScore() {
    return score;
  }

  public static void addPoint(Player player) {
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

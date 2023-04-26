package victor.testing.tdd;

public class TennisScore {

  private static String score = "Love-Love";

  public static String getScore() {
    return score;
  }

  public static void winsPoint(Player player) {
    score = "Fifteen-Love";
  }
}

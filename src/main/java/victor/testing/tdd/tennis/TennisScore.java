package victor.testing.tdd.tennis;

public class TennisScore {

  private static String score = "Love-Love";

  public static String getScore() {
    return score;
  }

  public static void player1Scored() {
    score = "Fifteen-Love";
  }
}

package victor.testing.tdd;

public class TennisScore {

  private static String string = "Love - Love";

  public static String getScore() {
    return string;
  }

  public static void addPointToPlayer1() {
    string = "Fifteen - Love";
  }
}

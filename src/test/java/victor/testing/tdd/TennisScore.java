package victor.testing.tdd;

public class TennisScore {

  private String string = "Love - Love";

  public String getScore() {
    return string;
  }

  public void addPointToPlayer1() {
    if (string.equals("Love - Love")) {
      string = "Fifteen - Love";
    } else if (string.equals("Fifteen - Love")) {
      string = "Thirty - Love";
    }
  }
}

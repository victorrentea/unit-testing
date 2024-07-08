package victor.testing.tdd;

public class TennisScore {

  private String string = "Love - Love";

  public String getScore() {
    return string;
  }

  public void addPointToPlayer1() {
    string = "Fifteen - Love";
  }
}

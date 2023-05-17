package victor.testing.tdd.classic;

public class TennisGame {

  //    if *
  private String string = "Love:Love";

  public String getScore() {
    return string;
  }

  public void addPoint(Player player) {
    if (string.equals("Fifteen:Love")) {
      string = "Thirty:Love";
    } else {
      string = "Fifteen:Love";
    }
  }
}

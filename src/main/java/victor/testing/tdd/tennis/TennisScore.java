package victor.testing.tdd.tennis;

public class TennisScore {

  private String score = "Love-Love";

  public String getScore() {
    return score;
  }

  public void player1Scored() {
    if (score.equals("Love-Love")) {
      score = "Fifteen-Love";
    } else {
      score = "Thirty-Love";
    }
  }
}

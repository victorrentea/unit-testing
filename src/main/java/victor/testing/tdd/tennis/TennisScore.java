package victor.testing.tdd.tennis;

public class TennisScore {

  private String score = "Love-Love";

  public String getScore() {
    return score;
  }

  public void player1Scored() {
    score = "Fifteen-Love";
  }
}

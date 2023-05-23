package victor.testing.tdd.tennis;

public class TennisGame {

  private String score = "Love-Love";
//  private String firstTeamScore = "Love";...
//  private String secondTeamScore = "Love";...

  public String getScore() {
    return score;
  }

  public void addPointToFirstTeam() {
    score = "Fifteen-Love";
  }

  public void addPointToSecondTeam() {
    if (score.equals("Fifteen-Love")) {
      score = "Fifteen-Fifteen";
    } else {
      score = "Love-Fifteen";
    }
  }
}

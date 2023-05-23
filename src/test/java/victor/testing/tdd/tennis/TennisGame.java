package victor.testing.tdd.tennis;

public class TennisGame {
  private String firstTeamScore = "Love";
  private String secondTeamScore = "Love";

  public String getScore() {
    return firstTeamScore + "-" + secondTeamScore;
  }
  // TODO make a switch

  public void addPointToFirstTeam() {
    firstTeamScore = "Fifteen";
  }

  public void addPointToSecondTeam() {
    secondTeamScore = "Fifteen";
  }
}

package victor.testing.tdd.tennis;

import java.util.List;

public class TennisGame {
  List<String> scoreNames = List.of("Love", "Fifteen", "Thirty", "Forty");
  private int firstTeamScore;
  private int secondTeamScore;

  public String getScore() {
    return scoreNames.get(firstTeamScore) + "-" + scoreNames.get(secondTeamScore);
  }

  public void addPointToFirstTeam() {
    firstTeamScore++;
  }

  public void addPointToSecondTeam() {
    secondTeamScore++;
  }
}

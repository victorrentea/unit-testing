package victor.testing.tdd.tennis;

import lombok.Setter;

import java.util.List;

//@Setter omg no: breaks encapsulation JUST FOR TESTING
public class TennisGame {
  List<String> scoreNames = List.of("Love", "Fifteen", "Thirty", "Forty");
  private int firstTeamScore;
  private int secondTeamScore;

  // JUST FOR TESTING- NO don't do IT
//  public void setPoints(int a, int b) {
//    firstTeamScore =a;
//    secondTeamScore = b;
//  }
  public String getScore() {
    if (Math.abs(firstTeamScore - secondTeamScore) == 1 &&
        firstTeamScore >= 3 && secondTeamScore >= 3) {
      return "Advantage";
    }
    if (firstTeamScore == secondTeamScore &&
        firstTeamScore >= 3) {
      return "Deuce";
    }



    return scoreNames.get(firstTeamScore) + "-" + scoreNames.get(secondTeamScore);
  }

  public void addPointToFirstTeam() {
    firstTeamScore++;
  }

  public void addPointToSecondTeam() {

    secondTeamScore++;
  }


}

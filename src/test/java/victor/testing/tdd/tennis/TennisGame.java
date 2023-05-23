package victor.testing.tdd.tennis;

public class TennisGame {
  enum ScoreName {
    LOVE("Love"),
    FIFTEEN("Fifteen"),
    THIRTY("Thirty"),
    FORTY("Forty");
    public final String name;

    ScoreName(String name) {
      this.name = name;
    }
  }
  private ScoreName firstTeamScore = ScoreName.LOVE;
  private ScoreName secondTeamScore = ScoreName.LOVE;

  public String getScore() {
    return firstTeamScore.name + "-" + secondTeamScore.name;
  }
  // TODO make a switch

  public void addPointToFirstTeam() {
    if (firstTeamScore == ScoreName.THIRTY) {
      firstTeamScore = ScoreName.FORTY;
    } else if (firstTeamScore == ScoreName.FIFTEEN) {
      firstTeamScore = ScoreName.THIRTY;
    } else {
      firstTeamScore = ScoreName.FIFTEEN;
    }
  }

  public void addPointToSecondTeam() {
    secondTeamScore = ScoreName.FIFTEEN;
  }
}

package ro.victor.unittest.tdd;

public class TennisScore {


  private int playerOneScore = 0;
  private int playerTwoScore = 0;

  private static final String[] SCORES = {"Love", "Fifteen", "Thirty", "Forty"};

  public String score() {
    if (playerOneScore >= 4 && (playerOneScore - playerTwoScore) >= 2) {
      return "Winner Player One";
    }

    if (playerTwoScore >= 4 && (playerTwoScore - playerOneScore) >= 2) {
      return "Winner Player Two";
    }

    if (playerOneScore >= 3 && playerTwoScore >= 3) {
      if (playerTwoScore == playerOneScore) {
        return "Deuce";
      } else {
        if (playerOneScore > playerTwoScore) {
          return "Advantage Player One";
        }else{
          return "Advantage Player Two";
        }
      }
    }


    return SCORES[playerOneScore] + "-" + SCORES[playerTwoScore];
  }

  public void addPointPlayerOne() {
    playerOneScore++;
  }

  public void addPointPlayerTwo() {
    playerTwoScore++;
  }
}

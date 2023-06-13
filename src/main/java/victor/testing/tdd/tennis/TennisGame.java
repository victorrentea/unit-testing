package victor.testing.tdd.tennis;

public class TennisGame {
  private int player1Points = 0;
  private int player2Points = 0;

  public String getHumanReadableScore() {
    if (player1Points >= 3 && player2Points >= 3) {
      return "Deuce";
    }
    return translatePointsToScore(player1Points) +
           "-" +
           translatePointsToScore(player2Points);
  }

  private String translatePointsToScore(int points) {
    return switch (points) {
      case 0 -> "Love";
      case 1 -> "Fifteen";
      case 2 -> "Thirty";
      default -> "Forty";
    };
  }

  public void addPointToPlayer1() {
    player1Points++;
  }

  public void addPointToPlayer2() {
    player2Points++;
  }
}

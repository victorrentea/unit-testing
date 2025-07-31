package victor.testing.tennis;

public class TennisScore {
  private final int player1Score;
  private final int player2Score;

  public TennisScore(int player1Score, int player2Score) {
    this.player1Score = player1Score;
    this.player2Score = player2Score;
  }

  public String getScore() {
    return mapScore(player1Score)+"-" + mapScore(player2Score);
  }

  public String mapScore(int score) {
    return switch (score) {
      case 0 -> "Love";
      case 1 -> "Fifteen";
      case 2 -> "Thirty";
      default -> throw new IllegalArgumentException();
    };
  }
}

package victor.testing.tdd.classic;

public class TennisGame {
  private int player1Points;
  private int player2Points;

  public String getScore() {
    if (player1Points == player2Points &&
        player1Points >= 3) {
      return "Deuce";
    }
    return getPlayerScore(player1Points) +
           ":" +
           getPlayerScore(player2Points);
  }

  private String getPlayerScore(int points) {
    return switch (points) {
      case 0 -> "Love";
      case 1 -> "Fifteen";
      case 2 -> "Thirty";
      case 3 -> "Forty";
      default -> throw new IllegalStateException("Unk points: " + points);
    };
  }

  public void addPoint(Player player) {
    if (player == Player.ONE) {
      player1Points++;
    } else {
      player2Points++;
    }
  }
}

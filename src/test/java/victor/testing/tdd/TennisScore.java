package victor.testing.tdd;

public class TennisScore {
  private int player1Points = 0;
  private int player2Points = 0;

  public String getScore() {
    return translate(player1Points) + ":" + translate(player2Points);
  }

  private String translate(int points) {
    return switch (points) {
      case 0 -> "Love";
      case 1 -> "Fifteen";
      case 2 -> "Thirty";
      case 3 -> "Forty";
      default -> throw new IllegalArgumentException();
    };
  }

  public void addPoint(int player) {
    if (player == 1) {
      player1Points++;
    } else {
      player2Points++;
    }
  }
}

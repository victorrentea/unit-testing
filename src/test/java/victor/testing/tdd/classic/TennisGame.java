package victor.testing.tdd.classic;

public class TennisGame {
  private int player1Points;
  private int player2Points;

  public String score() {
    if (player1Points == player2Points &&
        player1Points >=3) {
      return "Deuce";
    }
    return translatePoints(player1Points) +
           " - " + translatePoints(player2Points);
  }

  private String translatePoints(int points) {
    return switch (points) {
      case 0 -> "Love";
      case 1 -> "Fifteen";
      case 2 -> "Thirty";
      case 3 -> "Forty";
      default -> throw new IllegalArgumentException("No translation for " + points);
    };
  }

  public void earnsPoint(TennisSide side) {
    if (side == TennisSide.ONE) {
      player1Points++;
    } else {
      player2Points++;
    }
  }
}

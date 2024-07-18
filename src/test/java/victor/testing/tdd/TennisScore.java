package victor.testing.tdd;

public class TennisScore {
  private int player1Points = 0;
  private int player2Points = 0;

  public String getScore() {
    return translate(player1Points) + ":" + translate(player2Points);
  }

  private String translate(int points) {
    if (points == 0) {
      return "Love";
    }
    if (points == 1) {
      return "Fifteen";
    }
    if (points == 2) {
      return "Thirty";
    }
    throw new IllegalArgumentException();
  }

  public void addPoint(int player) {
    if (player == 1) {
      player1Points++;
    } else {
      player2Points++;
    }
  }
}

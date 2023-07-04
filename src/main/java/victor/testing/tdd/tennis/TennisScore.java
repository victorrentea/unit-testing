package victor.testing.tdd.tennis;

public class TennisScore {
  private int player1Points = 0;
  private int player2Points = 0;

  public String getScore() {
    if (player1Points == player2Points &&
        player1Points >= 3) {
      return "Deuce";
    }
    return convert(player1Points) +
           ":" + convert(player2Points);
  }

//  Map<Intege,r Stirng >

  private String convert(int points) {
    switch (points) {
      case 0:
        return "Love";
      case 1:
        return "Fifteen";
      case 2:
        return "Thirty";
      default:
        return "Forty";
    }
  }

  public void addPoint(Player player) {
    if (player == Player.ONE) {
      player1Points++;
    } else {
      player2Points++;
    }
  }
}
//  private Player{int points; String name;} player1;

// enum {LOVE,FIFTEEN,...}

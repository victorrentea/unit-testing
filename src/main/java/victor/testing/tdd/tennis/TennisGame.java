package victor.testing.tdd.tennis;

//enum Score {
//  Love(0), Fifteen(1)
//}
//  Map<Integer,String> = {
//    0:"Love",
//    1:"FIfteen",
//  }
public class TennisGame {
  private static final String SCORES[]
      = {"Love", "Fifteen","Thirty", "Forty"};
  private int player1Points = 0;
  private int player2Points = 0;

  public String getScore() {
    if (player1Points >= 3
        && player1Points == player2Points) {
      return "Deuce";
    }
    return translate(player1Points) + "-" + translate(player2Points);
  }

  private String translate(int points) {
    return SCORES[points];
  }

  public void addPoint(Player player) {
    if (player == Player.ONE) {
      player1Points++;
    } else {
      player2Points++;
    }
  }
}
